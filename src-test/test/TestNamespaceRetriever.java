package test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import info.semantictext.NamespaceProcessor;
import info.semantictext.NamespaceRetriever;
import info.semantictext.ParseException;
import info.semantictext.Parser;

public class TestNamespaceRetriever
{
    public static void main(String[] args) throws IOException, ParseException
    {
	new TestNamespaceRetriever().mainTest();
    }   
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        Parser parser = new Parser();
        NamespaceProcessor nsProcessor = new NamespaceProcessor();
        parser.addNodeProcessor(nsProcessor);
        
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
