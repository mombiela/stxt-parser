package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import info.semantictext.Node;
import info.semantictext.ParseException;
import info.semantictext.Parser;

public class TestParserBasicAll
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParserBasicAll().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        // Create parser
        Parser parser = createBasicParser();
        
        List<File> files = new ArrayList<>();
        for (File f: new File("namespaces").listFiles()) files.add(f);
        
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

    private Parser createBasicParser()
    {
        Parser parser = new Parser();
        return parser;
    }

}
