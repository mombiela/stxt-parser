package test.parser_legacy;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.legacy.Node;
import info.legacy.UtilsFile;

public class TestParserX00 extends AbstractTestParser
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        String content = UtilsFile.readFileContent(new File("examples/contribute.stxt"));
        parser.parse(content);
        Node n = parser.getDocumentNode();
        System.out.println(n);
        System.out.println("********************************");
        System.out.println(n.toSTXT());
        System.out.println("********************************");
        
        System.out.println("End");
    }

}
