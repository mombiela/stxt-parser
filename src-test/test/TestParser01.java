package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import info.Namespace;
import info.Node;
import info.ParseException;

public class TestParser01 extends AbstractTestParser
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParser01().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        File[] files = new File("namespaces").listFiles();
        for (File f: files)
        {
            if (!f.getName().equals("doc_simple.stxt")) continue;
            
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(f.getAbsolutePath());
            List<Node> docs = parser.parseFile(f);
            Node n = docs.get(0);
            System.out.println(n);
        }
        for (Namespace n: grammarProcessor.getNamespaces())
        {
            System.out.println("======================================================");
            System.out.println(n);
        }
        
        System.out.println("End");
    }
}
