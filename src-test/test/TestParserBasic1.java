package test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import info.Node;
import info.NodeBasicProcessor;
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
        List<Node> docs = parser.parseFile(f);
        Node n = docs.get(0);
        System.out.println(n);
        System.out.println("'" + n.getChild("cuerpo").getValue() + "'");
        
        System.out.println("End");
    }

    private Parser createBasicParser(Set<String> multilineNodes)
    {
        Parser parser = new Parser();
        NodeBasicProcessor basicProcessor = new NodeBasicProcessor();
        basicProcessor.setMultilineNodes(multilineNodes);
        basicProcessor.debug = true;
        parser.addNodeProcessor(basicProcessor);
        
        return parser;
    }

}
