package test;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.Node;
import info.semantictext.Parser;

public class TestParser02
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        Parser p = new Parser();
        p.parse(new File("defs/www.semantictext.info/namespace.es.stxt"));
        Node n = p.getDocumentNode();
        System.out.println(n);
        System.out.println(n.toSTXT());
        
        System.out.println("End");
    }

}
