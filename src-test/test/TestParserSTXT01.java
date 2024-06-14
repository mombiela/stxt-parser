package test;

import java.io.File;
import java.util.List;

import info.NamespaceRetriever;
import info.Node;
import info.STXTParser;

public class TestParserSTXT01
{
    public static void main(String[] args) throws Exception
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
