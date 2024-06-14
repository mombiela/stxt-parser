package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import info.semantictext.BasicProcessor;
import info.semantictext.Node;
import info.semantictext.ParseException;
import info.semantictext.Parser;

public class TestParserAllDocs
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParserAllDocs().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        Set<String> multilineNodes = new HashSet<>();
        multilineNodes.add("contenido");
        multilineNodes.add("cuerpo");
        multilineNodes.add("Foto");
        multilineNodes.add("observacion");    
        multilineNodes.add("Preparación");
        multilineNodes.add("text");
        multilineNodes.add("Descripción");
        multilineNodes.add("Observaciones");
        multilineNodes.add("binary");
        multilineNodes.add("hexadecimal");
        multilineNodes.add("base64");
        
        // Create parser
        Parser parser = createBasicParser(multilineNodes);
        
        List<File> files = new ArrayList<>();
        for (File f: new File("docs").listFiles()) files.add(f);
        for (File f: new File("examples").listFiles()) files.add(f);
        
        for (File f: files)
        {
            if (!f.getName().endsWith(".stxt")) continue;
            
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(f.getAbsolutePath());
            List<Node> docs = parser.parseFile(f);
            Node n = docs.get(0);
            System.out.println(n);
        }
        
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
