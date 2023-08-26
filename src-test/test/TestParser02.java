package test;

import info.semantictext.Node;
import info.semantictext.parser.Parser;
import info.semantictext.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class TestParser02
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        InputStream in = null;
        try
        {
            in = new FileInputStream(new File("defs/www.semantictext.info/namespace.stxt"));
            Parser p = new Parser();
            p.parse(in);
            Node n = p.getDocumentNode();
            System.out.println(n);
            System.out.println(n.toSTXT());
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
        
        System.out.println("End");
    }

}
