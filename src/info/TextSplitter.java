package info;

public class TextSplitter
{
    private String prefix;
    private String centralText;
    private String suffix;

    public TextSplitter(String input)
    {
        splitText(input);
    }

    private void splitText(String input)
    {
        int prefixStart = input.indexOf("(");
        int prefixEnd = input.indexOf(")");

        if (prefixStart != -1 && prefixEnd != -1 && prefixEnd > prefixStart)
        {
            prefix = input.substring(prefixStart + 1, prefixEnd).trim();
            input = input.substring(prefixEnd + 1).trim();
        }
        else
        {
            prefix = "";
        }

        int suffixStart = input.lastIndexOf("(");
        int suffixEnd = input.lastIndexOf(")");

        if (suffixStart != -1 && suffixEnd != -1 && suffixEnd > suffixStart)
        {
            centralText = input.substring(0, suffixStart).trim();
            suffix = input.substring(suffixStart + 1, suffixEnd).trim();
        }
        else
        {
            centralText = input.trim();
            suffix = "";
        }
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getCentralText()
    {
        return centralText;
    }

    public String getSuffix()
    {
        return suffix;
    }
}
