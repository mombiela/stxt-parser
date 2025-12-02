package dev.stxt;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class NodeType {

    // --- Tipos single-line ---
    public static final String STRING    = "STRING";
    public static final String NUMBER    = "NUMBER";
    public static final String BOOLEAN   = "BOOLEAN";
    public static final String URL       = "URL";
    public static final String EMAIL     = "EMAIL";
    public static final String DATE      = "DATE";
    public static final String TIMESTAMP = "TIMESTAMP";

    // --- Tipos multi-line ---
    public static final String TEXT      = "TEXT";
    public static final String MARKDOWN  = "MARKDOWN";
    public static final String BASE64    = "BASE64";

    private static final Set<String> MULTILINE_TYPES   = new HashSet<>();
    private static final Set<String> SINGLELINE_TYPES  = new HashSet<>();
    private static final Set<String> ALL_TYPES         = new HashSet<>();

    static {
        // Single-line
        SINGLELINE_TYPES.add(STRING);
        SINGLELINE_TYPES.add(NUMBER);
        SINGLELINE_TYPES.add(BOOLEAN);
        SINGLELINE_TYPES.add(URL);
        SINGLELINE_TYPES.add(EMAIL);
        SINGLELINE_TYPES.add(DATE);
        SINGLELINE_TYPES.add(TIMESTAMP);

        // Multi-line
        MULTILINE_TYPES.add(TEXT);
        MULTILINE_TYPES.add(MARKDOWN);
        MULTILINE_TYPES.add(BASE64);

        // All
        ALL_TYPES.addAll(SINGLELINE_TYPES);
        ALL_TYPES.addAll(MULTILINE_TYPES);
    }

    private NodeType() {}

    public static String getDefault() {
        return STRING;
    }

    public static boolean isValidType(String type) {
        return type != null && ALL_TYPES.contains(type);
    }

    public static boolean isMultiline(String type) {
        return type != null && MULTILINE_TYPES.contains(type);
    }

    /**
     * Normaliza un tipo escrito en el documento a la constante canónica.
     * NO TIENE ALIAS. Todo es por coincidencia exacta en mayúsculas.
     * Ejemplo:
     *   "text"      -> TEXT
     *   " Markdown" -> MARKDOWN
     *   "BASE64"    -> BASE64
     *   "md"        -> null  (porque no existe ningún tipo "MD")
     */
    public static String normalize(String raw) {
        if (raw == null) return null;

        String upper = raw.trim().toUpperCase(Locale.ROOT);

        if (ALL_TYPES.contains(upper)) {
            return upper;
        }
        return null;
    }
}
