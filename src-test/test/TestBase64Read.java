package test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import info.semantictext.utils.FileUtils;

public class TestBase64Read 
{
    public static void main(String[] args) throws IOException {
	String text = new String(FileUtils.readFile(new File("examples/foto.base64.txt")));
	System.out.println(text);
	byte[] fotoBytes = Base64.getDecoder().decode(text);
	System.out.println(fotoBytes.length);
	FileUtils.saveByteArrayToFile(new File("examples/foto.gif"), fotoBytes);
    }
}
