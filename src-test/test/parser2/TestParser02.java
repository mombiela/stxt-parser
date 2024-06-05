package test.parser2;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.Node;

public class TestParser02 extends AbstractTestParser
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        parser.parseFile(new File("defs/www.semantictext.info/namespace.es.stxt"));
        Node n = parser.getDocumentNode();
        System.out.println(n);
        System.out.println(n.toSTXT());
        
        System.out.println("End");
    }

}
