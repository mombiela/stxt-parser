package test;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.Document;
import info.Node;
import info.ParseException;

public class TestParser01 extends AbstractTestParser
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParser01().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        File[] files = new File("defs").listFiles();
        for (File f: files)
        {
            if (!f.getName().equals("doc_simple.stxt")) continue;
            
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(f.getAbsolutePath());
            Document doc = parser.parseFile(f);
            Node n = doc.getDocuments().get(0);
            System.out.println(n);
        }
        
        System.out.println("End");
    }

}
