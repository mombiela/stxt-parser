package test;

import java.io.File;
import java.util.List;

import info.NamespaceRetriever;
import info.Node;
import info.STXTParser;

public class TestParserSTXT02
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Inici");
        
        // Creamos parser
        NamespaceRetriever namespaceRetriever = new NamespaceRetriever();
        namespaceRetriever.addGrammarDefinitionsFromDir(new File("defs"));
        STXTParser parser = new STXTParser(namespaceRetriever);
        
        // Ejecutamos con fichero
        List<Node> nodes = parser.parseFile(new File("docs/receta_1.stxt"));
        
        for (Node n: nodes)
        {
            System.out.println("****************************************************");
            System.out.println(n);
        }
        
        System.out.println("Fi");
    }
}
