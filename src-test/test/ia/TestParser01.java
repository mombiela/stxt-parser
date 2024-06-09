package test.ia;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.ia.Document;
import info.ia.Node;
import info.ia.ParserException;

public class TestParser01 extends AbstractTestParser
{
    public static void main(String[] args) throws IOException, ParserException
    {
        new TestParser01().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParserException
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
