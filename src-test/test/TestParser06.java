package test;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.Node;
import info.semantictext.parser.Parser;

public class TestParser06
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        Parser p = new Parser();
        p.parse(new File("examples/types.stxt"));
        Node n = p.getDocumentNode();
        System.out.println(n);
        System.out.println("********************************");
        System.out.println(n.toSTXT());
        System.out.println("********************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}
