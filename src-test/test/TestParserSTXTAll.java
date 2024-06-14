package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import info.NamespaceRetriever;
import info.Node;
import info.ParseException;
import info.STXTParser;

public class TestParserSTXTAll
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Inici");
        
        // Creamos parser
        NamespaceRetriever namespaceRetriever = new NamespaceRetriever();
        namespaceRetriever.addGrammarDefinitionsFromDir(new File("defs"));
        STXTParser parser = new STXTParser(namespaceRetriever);
        
        // Ejecutamos con fichero
        parseDir(new File("docs"), parser);
        parseDir(new File("examples"), parser);
        System.out.println("Fi");
    }

    private static void parseDir(File file, STXTParser parser) throws IOException, ParseException
    {
        File[] dirs = file.listFiles();
        for (File f: dirs)
        {
            if (!f.getName().endsWith(".stxt")) continue;
            parseFile(parser, f);
        }
    }

    private static void parseFile(STXTParser parser, File f) throws IOException, ParseException
    {
        System.out.println("********************************************************");
        System.out.println("Parse file: " + f.getAbsolutePath());
        List<Node> nodes = parser.parseFile(f);
        
        for (Node n: nodes)
        {
            System.out.println(n);
        }
    }
}
