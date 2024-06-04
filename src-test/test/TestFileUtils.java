package test;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.UtilsFile;

public class TestFileUtils
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        String filePath = "info/semantictext/namespace.stxt";
        String fileContent = UtilsFile.getFileContentFromClasspath(filePath);
        System.out.println(fileContent);
        
        System.out.println("End");
    }

}
