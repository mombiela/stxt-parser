package test.parser_old;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.old.Node;

public class TestParser04b extends AbstractTestParser
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        parser.parseFile(new File("examples/demo_compact2.stxt"));
        Node n = parser.getDocumentNode();
        System.out.println(n);
        System.out.println("********************************");
        System.out.println(n.toSTXT());
        System.out.println("********************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}
