package test;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.Document;
import info.Namespace;
import info.NamespaceChild;
import info.NamespaceNode;
import info.Node;
import info.ParseException;

public class TestParser02 extends AbstractTestParser
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParser02().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        File[] files = new File("defs").listFiles();
        for (File f: files)
        {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(f.getAbsolutePath());
            Document doc = parser.parseFile(f);
            Node n = doc.getDocuments().get(0);
            System.out.println(n);
        }
        for (Namespace n: grammarProcessor.getNamespaces())
        {
            System.out.println("======================================================");
            printNamespace(n);
        }
        
        System.out.println("End");
    }
    private void printNamespace(Namespace n)
    {
        System.out.println("Namespace: " + n.getName());
        for (String nodeName: n.getNodes().keySet())
        {
            NamespaceNode node = n.getNode(nodeName);
            System.out.println("NODE: " + node.getName() + ", type: " + node.getType());
            for (String childName: node.getChilds().keySet())
            {
                NamespaceChild child = node.getChilds().get(childName);
                System.out.println("\tChild: " + child);
            }
        }
    }
    

}
