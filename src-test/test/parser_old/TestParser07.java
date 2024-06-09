package test.parser_old;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.old.Node;
import info.old.UtilsFile;

public class TestParser07 extends AbstractTestParser
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        String content = UtilsFile.readFileContent(new File("examples/receta_bunyols.stxt"));
        parser.parse(content);
        Node n = parser.getDocumentNode();
        System.out.println(n);
        System.out.println("********************************");
        System.out.println(n.toSTXT());
        System.out.println("********************************");
        System.out.println(n.toSTXTCompact());
        
        System.out.println("End");
    }

}