package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import info.semantictext.Namespace;
import info.semantictext.NamespaceProcessor;
import info.semantictext.Node;
import info.semantictext.ParseException;
import info.semantictext.Parser;

public class TestNamespaceProcessorAll
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestNamespaceProcessorAll().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        Parser parser = new Parser();
        NamespaceProcessor nsProcessor = new NamespaceProcessor();
        parser.addNodeProcessor(nsProcessor);
        
        File[] files = new File("namespaces").listFiles();
        for (File f: files)
        {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(f.getAbsolutePath());
            List<Node> docs = parser.parseFile(f);
            Node n = docs.get(0);
            System.out.println(n);
        }
        for (Namespace n: nsProcessor.getNamespaces())
        {
            System.out.println("======================================================");
            System.out.println(n);
        }
        
        System.out.println("End");
    }
}
