package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.NamespaceType;
import info.semantictext.ParseException;

public class TestNamespaceNameValid
{
    public static void main(String[] args) throws IOException, ParseException
    {
        new TestNamespaceNameValid().mainTest();
    }
    
    @Test
    public void mainTest() throws IOException, ParseException
    {
        System.out.println("Inici");

        assertTrue(validNamespace("com.demo.example"));
        assertTrue(validNamespace("com.demo_hola.example"));
        assertTrue(validNamespace("co_m.demo_hola.example"));
        assertTrue(validNamespace("co-m.demo-hola.example"));
        assertTrue(validNamespace("Example"));
        assertFalse(validNamespace("com.demo..example"));
        
        System.out.println("End");
    }

    private boolean validNamespace(String ns)
    {
        return NamespaceType.isValidNamespace(ns, false);
    }
}
