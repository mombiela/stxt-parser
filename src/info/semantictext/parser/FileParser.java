package info.semantictext.parser;

import info.semantictext.Node;
import info.semantictext.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileParser
{
    public static Node parse(File srcFile) throws IOException
    {
        InputStream in = null;
        try
        {
            in = new FileInputStream(srcFile);
            Parser p = new Parser();
            p.parse(in);
            return p.getDocumentNode();
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }        
    }
}
