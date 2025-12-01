package test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import dev.stxt.parser.NamespaceRetriever;
import dev.stxt.parser.ParseException;

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
        
        NamespaceRetriever nsRetriver = new NamespaceRetriever();
        nsRetriver.addGrammarDefinitionsFromFile(f);
        
        Set<String> namespaces = nsRetriver.getAllNamespaces();
        for (String namespace: namespaces)
        {
            System.out.println("*************************************************************");
            System.out.println(namespace);
            System.out.println(nsRetriver.getNameSpace(namespace));
        }
        
        System.out.println("End");
    }
}
