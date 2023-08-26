package info.semantictext.utils;

public class StringCleanup {
    public static void main(String[] args) {
        String input = "Hola,\n\n\neste es un ejemplo\r\ncon    múltiples espacios\t\t\ty saltos de línea.";
        String cleanedString = cleanupString(input);
        System.out.println(cleanedString);
    }

    public static String cleanupString(String input) {
        return input.replaceAll("[\\r\\n\\t]+|\\s+", "");
    }
}