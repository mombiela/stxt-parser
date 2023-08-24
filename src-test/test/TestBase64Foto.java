package test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import info.semantictext.utils.FileUtils;

public class TestBase64Foto
{
    public static void main(String[] args) throws IOException {
        byte[] btts = FileUtils.readFile(new File("examples/foto.gif"));
        System.out.println(Base64.getEncoder().encode(btts));
    }
    
    public static byte[] readFile (String file) throws IOException {
        return FileUtils.readFile(new File(file));
    }
}
