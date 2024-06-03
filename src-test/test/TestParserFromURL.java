package test;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.Node;
import info.semantictext.parser.Parser;

public class TestParserFromURL
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        Parser p = new Parser();
        p.parseURI("https://semantictext.info/es/chapter_02.stxt");
        Node n = p.getDocumentNode();
        System.out.println(n);
        System.out.println("************************");
        System.out.println(n.toSTXT());
        System.out.println("************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}
