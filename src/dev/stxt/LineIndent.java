package dev.stxt;

import static dev.stxt.Constants.SPACE;
import static dev.stxt.Constants.TAB;
import static dev.stxt.Constants.TAB_SPACES;

public class LineIndent
{
    // Indentation mode constants
    private static final int MODE_NONE = 0;
    private static final int MODE_SPACES = 1;
    private static final int MODE_TABS = 2;
    
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
        return line.startsWith("#");
    }

    // -------------------------------------------------
    // parseLine
    // -------------------------------------------------

    public static LineIndent parseLine(String aLine, int numLine, ParseState parseState) throws ParseException
    {
        int stackSize = parseState.getStack().size();
        boolean lastNodeMultiline = stackSize > 0 && parseState.getStack().peek().isMultiline();

        // 2) Cálculo de indentación con reglas estrictas (sin mezclar)
        String line = aLine;
        int mode = MODE_NONE;
        int level = 0;
        int spaces = 0;
        int pointer = 0;
        int length = line.length();

        while (pointer < length)
        {
            char c = line.charAt(pointer);

            if (c == SPACE)
            {
                if (mode == MODE_NONE)		mode = MODE_SPACES;
                else if (mode == MODE_TABS)	throw new ParseException(numLine, "MIXED_INDENTATION", "Cannot mix spaces and tabs in indentation");

                spaces++;
                if (spaces == TAB_SPACES)
                {
                    level++;
                    spaces = 0;
                }
            }
            else if (c == TAB)
            {
                if (mode == MODE_NONE)			mode = MODE_TABS;
                else if (mode == MODE_SPACES)	throw new ParseException(numLine, "MIXED_INDENTATION", "Cannot mix spaces and tabs in indentation");
                level++;
            }
            else
            {
                // Primer carácter no espacio/tab → fin de indentación
                break;
            }

            pointer++;

            // Dentro de multilínea: no consumir más niveles de los del nodo
            // ==> TEXT
            if (lastNodeMultiline && level >= stackSize)
            {
            	return new LineIndent(level, line.substring(pointer));
            }
        }
        
        // Line without indent
        line = line.substring(pointer);
        boolean emptyLine = isEmptyLine(line);
        
        // Multiline
        if (lastNodeMultiline && emptyLine)
        {
        	return new LineIndent(stackSize, "");
        }
        
        // Validar espacios sueltos si estamos en modo SPACES
        if (mode == MODE_SPACES && spaces != 0 && !emptyLine)
        {
            throw new ParseException(numLine, "INVALID_INDENTATION_SPACES", "Invalid number of spaces for indentation");
        }

        if (isCommentLine(line) || emptyLine)
        {
            // Comentario real fuera del bloque de texto
            return null;
        }
        
        // 4) Caso general: devolver la línea sin la indentación consumida
        return new LineIndent(level, line);
    }
}
