package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import info.semantictext.utils.FileUtils;
import info.semantictext.utils.StringCleanup;
import org.junit.jupiter.api.Test;

public class Base64Read 
{
    @Test
    public void mainTest() throws IOException {
	String text = FileUtils.readFileContent(new File("examples/foto.base64.txt"));
	System.out.println(text);
	byte[] fotoBytes = Base64.getDecoder().decode(StringCleanup.cleanupString(text));
	System.out.println(fotoBytes.length);
	
	byte[] fotoBytesReal = FileUtils.readFile(new File("examples/foto.gif"));
	assertTrue(Arrays.equals(fotoBytes, fotoBytesReal));
    }
}
