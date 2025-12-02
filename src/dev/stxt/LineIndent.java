package dev.stxt;

import static dev.stxt.Constants.SPACE;
import static dev.stxt.Constants.TAB;
import static dev.stxt.Constants.TAB_SPACES;

public class LineIndent
{
    public final int indentLevel;
    public final String lineWithoutIndent;

    private LineIndent(int level, String line)
    {
        this.indentLevel = level;
        this.lineWithoutIndent = line;
    }

    // -------------------------------------------------
    // Helpers conceptuales (equivalentes a los regex)
    // -------------------------------------------------

    private static boolean isEmptyLine(String line)
    {
        return line.trim().isEmpty();
    }

    private static boolean isCommentLine(String line)
    {
        return line.trim().startsWith("#");
    }

    /**
     * Línea compacta: "N:contenido" - Debe contener ':'. - Todo lo que hay
     * antes del ':' deben ser dígitos. - No se permiten espacios delante.
     */
    private static boolean isCompactLine(String line)
    {
        if (line == null || line.isEmpty())
        {
            return false;
        }

        int posColon = line.indexOf(':');
        if (posColon <= 0)
        {
            // No hay ':' o está en la primera posición (":algo")
            return false;
        }

        // Prefijo antes de ':' debe ser todo dígitos
        for (int i = 0; i < posColon; i++)
        {
            char c = line.charAt(i);
            if (!Character.isDigit(c))
            {
                return false;
            }
        }

        return true;
    }

    // -------------------------------------------------
    // parseLine
    // -------------------------------------------------

    public static LineIndent parseLine(String aLine, int numLine, ParseState parseState) throws ParseException
    {
        int stackSize = parseState.getStack().size();
        boolean lastNodeMultiline = stackSize > 0 && NodeType.isMultiline(parseState.getStack().peek().getType());

        String trimmed = aLine.trim();

        // 0) Prioridad absoluta: línea compacta "N:contenido"
        if (isCompactLine(aLine))
        {
            int posColon = aLine.indexOf(':');
            String levelStr = aLine.substring(0, posColon);
            String textStr = aLine.substring(posColon + 1);

            int level;
            try
            {
                level = Integer.parseInt(levelStr);
            }
            catch (NumberFormatException e)
            {
                throw new ParseException(numLine, "INVALID_COMPACT_LEVEL", "Invalid compact indentation level: " + levelStr);
            }

            return new LineIndent(level, textStr);
        }

        // 1) Si NO estamos en multilínea: líneas vacías y comentarios se
        // ignoran
        if (!lastNodeMultiline)
        {
            if (trimmed.isEmpty())
            {
                return null;
            }
            if (trimmed.startsWith("#"))
            {
                return null;
            }
        }

        // 2) Cálculo de indentación con reglas estrictas (sin mezclar)
        String line = aLine;
        String mode = "NONE"; // "NONE", "SPACES", "TABS"
        int level = 0;
        int spaces = 0;
        int pointer = 0;
        int length = line.length();

        while (pointer < length)
        {
            char c = line.charAt(pointer);

            if (c == SPACE)
            {
                if ("NONE".equals(mode))
                {
                    mode = "SPACES";
                }
                else if ("TABS".equals(mode))
                {
                    throw new ParseException(numLine, "MIXED_INDENTATION", "Cannot mix spaces and tabs in indentation");
                }

                spaces++;
                if (spaces == TAB_SPACES)
                {
                    level++;
                    spaces = 0;
                }

            }
            else if (c == TAB)
            {
                if ("NONE".equals(mode))
                {
                    mode = "TABS";
                }
                else if ("SPACES".equals(mode))
                {
                    throw new ParseException(numLine, "MIXED_INDENTATION", "Cannot mix spaces and tabs in indentation");
                }
                level++;

            }
            else
            {
                // Primer carácter no espacio/tab → fin de indentación
                break;
            }

            pointer++;

            // Dentro de multilínea: no consumir más niveles de los del nodo
            // TEXT
            if (lastNodeMultiline && level >= stackSize)
            {
                break;
            }
        }

        // Validar espacios sueltos si estamos en modo SPACES
        if ("SPACES".equals(mode) && spaces != 0)
        {
            throw new ParseException(numLine, "INVALID_INDENTATION_SPACES", "Invalid number of spaces for indentation");
        }

        // 3) Caso especial: venimos de nodo multilínea y hemos dedentado
        if (lastNodeMultiline && level < stackSize)
        {
            if (isEmptyLine(aLine))
            {
                // Preservamos la línea vacía como parte del texto del bloque
                return new LineIndent(stackSize, "");
            }
            if (isCommentLine(aLine))
            {
                // Comentario real fuera del bloque de texto
                return null;
            }
            // Cualquier otro caso: la línea se tratará como estructural por el
            // parser
        }

        // 4) Caso general: devolver la línea sin la indentación consumida
        String lineWithoutIndent = aLine.substring(pointer);
        return new LineIndent(level, lineWithoutIndent);
    }
}
