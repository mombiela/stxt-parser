package test;

import java.io.File;
import java.io.IOException;

import info.semantictext.utils.Base64;
import info.semantictext.utils.FileUtils;

public class TestBase64Read 
{
    public static void main(String[] args) throws IOException {
	String text = new String(FileUtils.readFile(new File("examples/foto.base64.txt")));
	byte[] fotoBytes = Base64.decode(text);
	FileUtils.saveByteArrayToFile(new File("examples/foto.gif"), fotoBytes);
	System.out.println(text);
    }
}
