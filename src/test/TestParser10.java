package test;

import info.semantictext.Node;
import info.semantictext.parser.FileParser;

import java.io.File;
import java.io.IOException;

public class TestParser10
{
    public static void main(String[] args) throws IOException
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
