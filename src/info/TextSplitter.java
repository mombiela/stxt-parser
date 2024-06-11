package info;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextSplitter {
    private String prefix;
    private String centralText;
    private String suffix;

    private TextSplitter(String input) {
        splitText(input);
    }

    public static TextSplitter split(String input)
    {
        return new TextSplitter(input);
    }
    
    private void splitText(String input) {
        Pattern pattern = Pattern.compile("(\\(([^)]*)\\))?(.*?)(\\(([^)]*)\\))?$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            prefix = matcher.group(2) != null ? matcher.group(2).trim() : null;
            centralText = matcher.group(3) != null ? matcher.group(3).trim() : null;
            suffix = matcher.group(5) != null ? matcher.group(5).trim() : null;
        } else {
            centralText = input.trim();
            prefix = null;
            suffix = null;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCentralText() {
        return centralText;
    }

    public String getSuffix() {
        return suffix;
    }

}
