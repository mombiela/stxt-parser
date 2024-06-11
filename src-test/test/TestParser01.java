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
        
        Document doc = parser.parseFile(new File("defs/doc_simple.stxt"));

        Node n = doc.getDocuments().get(0);
        System.out.println(n);
        /*
        System.out.println("************************");
        System.out.println(n.toSTXT());
        System.out.println("************************");
        System.out.println(n.toSTXTCompact());
        */
        
        System.out.println("End");
    }

}
