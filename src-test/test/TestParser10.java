package test;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.Node;
import info.semantictext.Parser;

public class TestParser10
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        Parser p = new Parser();
    	p.parse(new File("examples/ws.stxt"));
        Node n = p.getDocumentNode();

        System.out.println(n);
        System.out.println("********************************");
        System.out.println(n.toSTXT());
        System.out.println("********************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}
