package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import info.semantictext.utils.StringCleanup;

class TestStringCleanup {

    @Test
    void test() {
        String input = "Hola,\n\n\neste es un ejemplo\r\ncon    múltiples espacios\t\t\ty saltos de línea.";
        String cleanedString = StringCleanup.cleanupString(input);
        assertEquals(cleanedString, "Hola,esteesunejemploconmúltiplesespaciosysaltosdelínea.");
    }

}
