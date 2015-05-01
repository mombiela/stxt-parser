package test;

import info.semantictext.Node;
import info.semantictext.parser.Parser;
import info.semantictext.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestParser2
{
    public static void main(String[] args) throws IOException
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
