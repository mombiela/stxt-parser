package test.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import dev.stxt.Parser;
import dev.stxt.Node;
import dev.stxt.ParseException;

public class TestParserAll
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestParserAll().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        // Create parser
        Parser parser = new Parser();
        File docsDir = new File("test/docs");

        List<File> stxtFiles;
        try (Stream<Path> stream = Files.walk(docsDir.toPath()))
        {
            // Discover all .stxt files in test/docs recursively
            stxtFiles = stream
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".stxt"))
                .map(Path::toFile)
                .sorted()
                .collect(Collectors.toList());
        }

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
            System.out.println(node.toJson().toString(3));
        }
    }

}