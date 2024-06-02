package test;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.utils.FileUtils;

public class TestFileUtils
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println("Inici");
        
        String filePath = "info/semantictext/grammar/namespace.stxt";
        String fileContent = FileUtils.getFileContentFromClasspath(filePath);
        System.out.println(fileContent);
        
        System.out.println("End");
    }

}
