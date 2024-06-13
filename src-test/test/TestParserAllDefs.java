package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import info.Namespace;
import info.NamespaceChild;
import info.NamespaceNode;
import info.Node;
import info.ParseException;

public class TestParserAllDefs extends AbstractTestParser
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParserAllDefs().mainTest();
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
            List<Node> docs = parser.parseFile(f);
            Node n = docs.get(0);
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
            System.out.println("NODE: " + node.getName() + ", type: " + node.getType() + " -> " + node.getValues());
            for (String childName: node.getChilds().keySet())
            {
                NamespaceChild child = node.getChilds().get(childName);
                System.out.println("\tChild: " + child);
            }
        }
    }
    

}
