package info;

import java.util.regex.Pattern;

public class LineIndent
{
    public static final String UTF8_BOM = "\uFEFF";

    public final int indentLevel;
    public final String lineWithoutIndent;
    
    private LineIndent(int level, String line)
    {
        this.indentLevel = level;
        this.lineWithoutIndent = line;
    }
    
    // ---------
    // Constants
    // ---------

    // Patterns
    private static final Pattern EMPTY_LINE = Pattern.compile("^\\s*$");
    private static final Pattern COMMENT_LINE = Pattern.compile("^\\s*\\#.*$");
    
    public static LineIndent parseLine(String aLine, boolean lastNodeMultiline, int stackSize) 
    {
        // Validate if empty line or comment
        if (!lastNodeMultiline)
        {
            if (EMPTY_LINE.matcher(aLine).matches() || COMMENT_LINE.matcher(aLine).matches())
            {
                return null;
            }
        }

        // Obtain the level and pointer
        int level = 0;
        int spaces = 0;

        int pointer = 0;
        while (pointer < aLine.length()) 
        {
            // Last char
            char charPointer = aLine.charAt(pointer);

            // Update level
            if (charPointer == Constants.SPACE) 
            {
                spaces++;
                if (spaces == Constants.TAB_SPACES) 
                {
                    level++;
                    spaces = 0;
                }
            } 
            else if (charPointer == Constants.TAB) 
            {
                level++;
                spaces = 0;
            }
            else 
            {
                break;
            }

            // Pointer position
            pointer++;

            // Validate that text can only have one more level, so no information is lost
            if (lastNodeMultiline && level >= stackSize) break;
        }

        // In case of text, check if it's a comment or not to preserve empty line (depends on the comment's level)
        if (lastNodeMultiline && level < stackSize)
        {
            if (EMPTY_LINE.matcher(aLine).matches())    return new LineIndent(stackSize, ""); // Preserve empty line
            if (COMMENT_LINE.matcher(aLine).matches())  return null; // It's a comment
        }

        return new LineIndent(level, aLine.substring(pointer));
    }

    public static String removeUTF8BOM(String s) 
    {
        if (s.startsWith(UTF8_BOM)) s = s.substring(1);
        return s;
    }
}
