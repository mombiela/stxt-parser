package info.semantictext.parser;

import java.io.File;
import java.io.IOException;

import info.semantictext.Node;
import info.semantictext.utils.FileUtils;

public class FileParser
{
    public static Node parse(File srcFile) throws IOException
    {
        String content = FileUtils.readFileContent(srcFile);
        Parser p = new Parser();
        p.parse(content);
        return p.getDocumentNode();
    }
}
