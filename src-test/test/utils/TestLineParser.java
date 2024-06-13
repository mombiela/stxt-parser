package test.utils;

import info.LineIndent;
import info.ParseException;

public class TestLineParser
{
    
    // ---------
    // Test Main
    // ---------

    public static void main(String[] args) throws ParseException {
        System.out.println("Start");

        // TODO Check all tests and make new
        System.out.println(LineIndent.parseLine("\t\t   \t    A recipe is the instructions, materials, etc.", false, 0, 1));
        System.out.println(LineIndent.parseLine("4:A recipe is the instructions, materials, etc.", false, 0, 1));
        System.out.println(LineIndent.parseLine("  #4:A recipe is the instructions, materials, etc.", false, 0, 1));
        System.out.println(LineIndent.parseLine("  #4:A recipe is the instructions, materials, etc.", true, 0, 1));
        System.out.println(LineIndent.parseLine("  \t   \t   ", false, 1, 1));
        System.out.println(LineIndent.parseLine("  \t   \t   ", true, 1, 1));
        System.out.println(LineIndent.parseLine("", true, 1, 1));

        System.out.println("End");
    }

}
