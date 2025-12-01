package dev.stxt.parser;

import java.util.Locale;

public class Utils
{
    public static String uniform(String name)
    {
        return name.trim().toLowerCase(Locale.ENGLISH);
    }

    public static String cleanupString(String input)
    {
        return input.replaceAll("[\\r\\n\\t]+|\\s+", "");
    }
}
