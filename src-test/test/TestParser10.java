package test;

import info.semantictext.Node;
import info.semantictext.parser.FileParser;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class TestParser10
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
    	Node n = FileParser.parse(new File("examples/ws.stxt"));

        System.out.println(n);
        System.out.println("********************************");
        System.out.println(n.toSTXT());
        System.out.println("********************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}
