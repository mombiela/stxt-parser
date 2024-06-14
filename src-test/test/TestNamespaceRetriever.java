package test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import info.NamespaceRetriever;
import info.ParseException;

public class TestNamespaceRetriever extends AbstractTestParser
{
    public static void main(String[] args) throws IOException, ParseException
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
