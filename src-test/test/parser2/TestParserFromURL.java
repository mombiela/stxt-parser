package test.parser2;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.Node;

public class TestParserFromURL extends AbstractTestParser
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        parser.parseURI("https://semantictext.info/es/chapter_02.stxt");
        Node n = parser.getDocumentNode();
        System.out.println(n);
        System.out.println("************************");
        System.out.println(n.toSTXT());
        System.out.println("************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}
