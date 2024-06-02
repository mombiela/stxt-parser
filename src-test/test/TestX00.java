package test;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.Node;
import info.semantictext.parser.Parser;
import info.semantictext.utils.FileUtils;

public class TestX00
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        String content = FileUtils.readFileContent(new File("examples/contribute.stxt"));
        Parser p = new Parser();
        p.parse(content);
        Node n = p.getDocumentNode();
        System.out.println(n);
        System.out.println("********************************");
        System.out.println(n.toSTXT());
        System.out.println("********************************");
        
        System.out.println("End");
    }

}
