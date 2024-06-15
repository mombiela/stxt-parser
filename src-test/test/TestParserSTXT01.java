package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import info.semantictext.NamespaceRetriever;
import info.semantictext.Node;
import info.semantictext.ParseException;
import info.semantictext.STXTParser;

public class TestParserSTXT01
{
    public static void main(String[] args) throws Exception
    {
	new TestParserSTXT01().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");
        
        // Creamos parser
        NamespaceRetriever namespaceRetriever = new NamespaceRetriever();
        namespaceRetriever.addGrammarDefinitionsFromDir(new File("namespaces"));
        STXTParser parser = new STXTParser(namespaceRetriever);
        
        // Ejecutamos con fichero
        File f = new File("docs/receta_1.stxt");
        System.out.println("FILE: " + f.getAbsolutePath());
        List<Node> nodes = parser.parseFile(f);
        
        for (Node n: nodes)
        {
            System.out.println("****************************************************");
            System.out.println(n);
        }
        
        System.out.println("Fi");
    }   
}
