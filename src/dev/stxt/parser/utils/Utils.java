package dev.stxt.parser.utils;

public class Utils
{
    public static final String UTF8_BOM = "\uFEFF";

    private Utils() 
    {
        // Evita instanciación
    }

    public static String removeUTF8BOM(String s) 
    {
        if (s.startsWith(UTF8_BOM)) s = s.substring(1);
        return s;
    }
    
    public static String cleanupString(String input)
    {
        return input.replaceAll("[\\r\\n\\t]+|\\s+", "");
    }
}