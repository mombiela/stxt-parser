package test.parser2;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.old.Node;

public class TestParser03 extends AbstractTestParser
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        parser.parseFile(new File("examples/demo.stxt"));
    	Node n = parser.getDocumentNode();
    	System.out.println(n);
    	System.out.println(n.toSTXT());
        
        System.out.println("End");
    }

}
