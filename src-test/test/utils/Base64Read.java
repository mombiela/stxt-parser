package test.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import info.semantictext.Utils;
import info.semantictext.UtilsFile;

public class Base64Read 
{
    @Test
    public void mainTest() throws IOException {
	String text = UtilsFile.readFileContent(new File("docs/foto.base64.txt"));
	System.out.println(text);
	byte[] fotoBytes = Base64.getDecoder().decode(Utils.cleanupString(text));
	System.out.println(fotoBytes.length);
	
	byte[] fotoBytesReal = UtilsFile.readFile(new File("docs/foto.gif"));
	assertTrue(Arrays.equals(fotoBytes, fotoBytesReal));
    }
}
