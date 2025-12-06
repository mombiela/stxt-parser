package test.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import dev.stxt.Node;
import dev.stxt.ParseException;
import dev.stxt.Parser;
import dev.stxt.utils.FileUtils;

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

        // Create parser
        Parser parser = new Parser();
        File docsDir = new File("test/docs");

        List<File> stxtFiles = FileUtils.getStxtFiles(docsDir);

        for (File file : stxtFiles)
        {
            validateFile(parser, file);
        }

        System.out.println("End");
    }

    private void validateFile(Parser parser, File file) throws IOException, ParseException
    {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(file.getAbsolutePath());
        List<Node> docs = parser.parseFile(file);
        for (Node node : docs)
        {
            System.out.println(node.toJsonPretty());
        }
    }

}