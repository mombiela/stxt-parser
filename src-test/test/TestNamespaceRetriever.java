package test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import stxt.parser.NamespaceRetriever;
import stxt.parser.ParseException;

public class TestNamespaceRetriever
{
    public static void main(String[] args) throws IOException, ParseException
    {
	new TestNamespaceRetriever().mainTest();
    }   
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        NamespaceRetriever nsRetriver = new NamespaceRetriever();
        nsRetriver.addGrammarDefinitionsFromDir(new File("namespaces"));
        Set<String> namespaces = nsRetriver.getAllNamespaces();
        
        for (String namespace: namespaces)
        {
            System.out.println("*************************************************************");
            System.out.println(namespace);
            System.out.println(nsRetriver.getNameSpace(namespace));
        }
    }
}
