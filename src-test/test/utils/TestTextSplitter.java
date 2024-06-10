package test.utils;

import info.TextSplitter;

public class TestTextSplitter
{
    public static void main(String[] args)
    {
        testSplit("(texto prefix) texto central (texto sufijo)");
        testSplit("texto central (texto sufijo)");
        testSplit("(texto prefix) texto central");
        testSplit("texto central");
    }

    private static void testSplit(String input)
    {
        TextSplitter splitter1 = new TextSplitter(input);
        System.out.println("*** INPUT: " + input);
        System.out.println("Prefix: " + splitter1.getPrefix());
        System.out.println("Central Text: " + splitter1.getCentralText());
        System.out.println("Suffix: " + splitter1.getSuffix());
    }
}
