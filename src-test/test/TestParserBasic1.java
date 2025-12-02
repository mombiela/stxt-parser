package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import info.semantictext.parser.Node;
import info.semantictext.parser.ParseException;
import info.semantictext.parser.Parser;

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

        // Create parser
        Parser parser = createBasicParser();
        
        File f = new File("docs/client_raw.stxt");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(f.getAbsolutePath());
        List<Node> docs = parser.parseFile(f);
        Node n = docs.get(0);
        System.out.println(n);
        
        System.out.println("End");
    }

    private Parser createBasicParser()
    {
        Parser parser = new Parser();
        
        return parser;
    }

}
