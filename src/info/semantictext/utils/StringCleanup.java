package info.semantictext.utils;

public class StringCleanup {
    public static String cleanupString(String input) {
        return input.replaceAll("[\\r\\n\\t]+|\\s+", "");
    }
}