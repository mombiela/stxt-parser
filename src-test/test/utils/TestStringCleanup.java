package test.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import info.semantictext.Utils;

class TestStringCleanup {

    @Test
    void test() {
        String input = "Hola,\n\n\neste es un ejemplo\r\ncon    múltiples espacios\t\t\ty saltos de línea.";
        String cleanedString = Utils.cleanupString(input);
        assertEquals(cleanedString, "Hola,esteesunejemploconmúltiplesespaciosysaltosdelínea.");
    }

}
