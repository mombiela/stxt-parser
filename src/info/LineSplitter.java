package info;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineSplitter 
{
    public final String prefix;
    public final String centralText;
    public final String suffix;

    private LineSplitter(String prefix, String centralText, String suffix) 
    {
        this.prefix = prefix;
        this.centralText = centralText;
        this.suffix = suffix;
    }

    public static LineSplitter split(String input)
    {
        String prefix, centralText, suffix;
        
        Pattern pattern = Pattern.compile("(\\(([^)]*)\\))?(.*?)(\\(([^)]*)\\))?$");
        Matcher matcher = pattern.matcher(input);
        
        if (matcher.matches()) 
        {
            prefix = matcher.group(2) != null ? matcher.group(2).trim() : null;
            centralText = matcher.group(3) != null ? matcher.group(3).trim() : null;
            suffix = matcher.group(5) != null ? matcher.group(5).trim() : null;
        }
        else 
        {
            centralText = input.trim();
            prefix = null;
            suffix = null;
        }
        if (centralText != null && centralText.length()==0) centralText = null;
        
        return new LineSplitter(prefix, centralText, suffix);
    }
}
