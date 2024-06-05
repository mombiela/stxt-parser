package test.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

class Base64FromString {
    @Test
    void testEncode() throws UnsupportedEncodingException {
        String text = "Hello World!!!!!!\nHola";
        String encoded = new String(Base64.getEncoder().encode(text.getBytes("UTF-8")));
        assertEquals("SGVsbG8gV29ybGQhISEhISEKSG9sYQ==", encoded);
    }
    
    @Test
    void testDecode() throws UnsupportedEncodingException {
        String encoded = "SGVsbG8gV29ybGQhISEhISEKSG9sYQ==";
        String decoded = new String(Base64.getDecoder().decode(encoded), "UTF-8");
        assertEquals(decoded, "Hello World!!!!!!\nHola");
    }
}
