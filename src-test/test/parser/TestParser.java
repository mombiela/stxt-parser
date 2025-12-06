package test.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import dev.stxt.Parser;
import dev.stxt.Node;
import dev.stxt.ParseException;

public class TestParser
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParser().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        // Create parser
        Parser parser = createBasicParser();
        
        File f = new File("test/docs/client_raw.stxt");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(f.getAbsolutePath());
        List<Node> docs = parser.parseFile(f);
        Node n = docs.get(0);
        System.out.println(n.toJson().toString(3));
        
        System.out.println("End");
    }

    private Parser createBasicParser()
    {
        Parser parser = new Parser();
        
        return parser;
    }

}
