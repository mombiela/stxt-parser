package test.utils;

import info.LineParser;

public class TestLineParser
{
    
    // ---------
    // Test Main
    // ---------

    public static void main(String[] args) {
        System.out.println("Start");

        // TODO Check all tests and make new
        System.out.println(LineParser.parseLine("\t\t   \t    A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(LineParser.parseLine("4:A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(LineParser.parseLine("  #4:A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(LineParser.parseLine("  #4:A recipe is the instructions, materials, etc.", true, 0));
        System.out.println(LineParser.parseLine("  \t   \t   ", false, 1));
        System.out.println(LineParser.parseLine("  \t   \t   ", true, 1));
        System.out.println(LineParser.parseLine("", true, 1));

        System.out.println("End");
    }

}
