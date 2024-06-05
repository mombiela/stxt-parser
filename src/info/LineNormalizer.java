package info;

import java.util.regex.Pattern;

public class LineNormalizer
{
    public static final String UTF8_BOM = "\uFEFF";

    // ---------
    // Constants
    // ---------

    // Patterns
    private static final Pattern COMPRESSED_LINE = Pattern.compile("^\\d+\\:.*$");
    private static final Pattern EMPTY_LINE = Pattern.compile("^\\s*$");
    private static final Pattern COMMENT_LINE = Pattern.compile("^\\s*\\#.*$");
    
    public static String normalize(String aLine, boolean lastNodeText, int lastLevel) {
        // Validate if already compressed
        if (COMPRESSED_LINE.matcher(aLine).matches())
            return aLine;

        // Validate if empty line or comment
        if (!lastNodeText && (EMPTY_LINE.matcher(aLine).matches() || COMMENT_LINE.matcher(aLine).matches()))
            return null;

        // Obtain the level and pointer
        int level = 0;
        int spaces = 0;

        int pointer = 0;
        while (pointer < aLine.length()) {
            // Last char
            char charPointer = aLine.charAt(pointer);

            // Update level
            if (charPointer == Constants.SPACE) {
                spaces++;
                if (spaces == Constants.TAB_SPACES) {
                    level++;
                    spaces = 0;
                }
            } else if (charPointer == Constants.TAB) {
                level++;
                spaces = 0;
            } else {
                break;
            }

            // Pointer position
            pointer++;

            // Validate that text can only have one more level, so no information is lost
            if (lastNodeText && level > lastLevel)
                break;
        }

        // In case of text, check if it's a comment or not (depends on the comment's level)
        if (lastNodeText && level <= lastLevel) {
            if (EMPTY_LINE.matcher(aLine).matches())
                return (lastLevel + 1) + ":";
            if (COMMENT_LINE.matcher(aLine).matches())
                return null;
        }

        return level + ":" + aLine.substring(pointer);
    }

    public static String removeUTF8BOM(String s) 
    {
        if (s.startsWith(UTF8_BOM)) s = s.substring(1);
        return s;
    }

    
    // ---------
    // Test Main
    // ---------

    public static void main(String[] args) {
        System.out.println("Start");

        System.out.println(normalize("\t\t   \t    A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(normalize("4:A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(normalize("  #4:A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(normalize("  #4:A recipe is the instructions, materials, etc.", true, 0));
        System.out.println(normalize("  \t   \t   ", false, 1));
        System.out.println(normalize("  \t   \t   ", true, 1));
        System.out.println(normalize("", true, 1));

        System.out.println("End");
    }

}
