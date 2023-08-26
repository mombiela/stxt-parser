package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.junit.jupiter.api.Test;

class Base64FromString {
    @Test
    void test() throws UnsupportedEncodingException {
        String text = "Hello World!!!!!!\nHola";
        String encoded = new String(Base64.getEncoder().encode(text.getBytes("UTF-8")));
        
        assertEquals("SGVsbG8gV29ybGQhISEhISEKSG9sYQ==", encoded);
    }
}
