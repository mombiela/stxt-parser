package test.utils;

import info.LineSplitter;

public class TestLineSplitter
{
    public static void main(String[] args)
    {
        testSplit("(texto prefix) texto central (texto sufijo)");
        testSplit("texto central (texto sufijo)");
        testSplit("(texto prefix) texto central");
        testSplit("texto central");
        testSplit("(otro)");
    }

    private static void testSplit(String input)
    {
        LineSplitter splitter1 = LineSplitter.split(input);
        System.out.println("*** INPUT: " + input);
        System.out.println("Prefix: " + splitter1.prefix);
        System.out.println("Central Text: " + splitter1.centralText);
        System.out.println("Suffix: " + splitter1.suffix);
    }
}
