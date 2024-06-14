package test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import info.semantictext.BasicProcessor;
import info.semantictext.Node;
import info.semantictext.ParseException;
import info.semantictext.Parser;

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
        multilineNodes.add("Foto");
        
        // Create parser
        Parser parser = createBasicParser(multilineNodes);
        
        File f = new File("docs/client.stxt");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(f.getAbsolutePath());
        List<Node> docs = parser.parseFile(f);
        Node n = docs.get(0);
        System.out.println(n);
        
        System.out.println("End");
    }

    private Parser createBasicParser(Set<String> multilineNodes)
    {
        Parser parser = new Parser();
        BasicProcessor basicProcessor = new BasicProcessor();
        basicProcessor.setMultilineNodes(multilineNodes);
        parser.addNodeProcessor(basicProcessor);
        
        return parser;
    }

}
