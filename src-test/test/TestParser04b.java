package test;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.Node;
import info.semantictext.Parser;

public class TestParser04b
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        Parser p = new Parser();
        p.parseFile(new File("examples/demo_compact2.stxt"));
        Node n = p.getDocumentNode();
        System.out.println(n);
        System.out.println("********************************");
        System.out.println(n.toSTXT());
        System.out.println("********************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}
