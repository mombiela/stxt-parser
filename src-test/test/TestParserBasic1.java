package test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import info.BasicProcessor;
import info.Document;
import info.Node;
import info.ParseException;
import info.Parser;

public class TestParserBasic1
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParserBasic1().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        Set<String> multilineNodes = new HashSet<>();
        multilineNodes.add("contenido");
        multilineNodes.add("cuerpo");
        
        // Create parser
        Parser parser = createBasicParser(multilineNodes);
        
        File f = new File("docs/receta_1.stxt");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(f.getAbsolutePath());
        Document doc = parser.parseFile(f);
        Node n = doc.getDocuments().get(0);
        System.out.println(n);
        System.out.println("'" + n.getChild("cuerpo").getValue() + "'");
        
        System.out.println("End");
    }

    private Parser createBasicParser(Set<String> multilineNodes)
    {
        Parser parser = new Parser();
        BasicProcessor basicProcessor = new BasicProcessor();
        basicProcessor.setMultilineNodes(multilineNodes);
        basicProcessor.debug = true;
        parser.addNodeProcessor(basicProcessor);
        
        return parser;
    }

}
