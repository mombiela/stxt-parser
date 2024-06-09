package test.parser_old;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.old.Node;

public class TestParser01 extends AbstractTestParser
{
    public static void main(String[] args) throws IOException
    {
        new TestParser01().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        parser.parseFile(new File("defs/www.kocinando.com/receta.stxt"));
        Node n = parser.getDocumentNode();
        System.out.println(n);
        System.out.println("************************");
        System.out.println(n.toSTXT());
        System.out.println("************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}
