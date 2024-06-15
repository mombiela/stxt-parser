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

public class TestNamespaceProcessor1
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestNamespaceProcessor1().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        File f = new File("namespaces/doc_simple.stxt");

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(f.getAbsolutePath());
        
        Parser parser = new Parser();
        NamespaceProcessor nsProcessor = new NamespaceProcessor();
        parser.addNodeProcessor(nsProcessor);
        
        parser.debug = true;
        List<Node> docs = parser.parseFile(f);
        Node n = docs.get(0);
        System.out.println(n);

        for (Namespace ns: nsProcessor.getNamespaces())
        {
            System.out.println("======================================================");
            System.out.println(ns);
        }
        
        System.out.println("End");
    }
}
