package info.semantictext.utils;

import java.util.Locale;

public class NameUtils
{
    public static String uniform(String name)
    {
        return name.trim().toLowerCase(Locale.ENGLISH);
    }
}
