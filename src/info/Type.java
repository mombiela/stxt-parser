package info;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Type 
{
    public static final String TEXT         = "TEXT";
    public static final String STRING       = "STRING";
    public static final String NUMBER       = "NUMBER";
    public static final String BOOLEAN      = "BOOLEAN";
    public static final String REGEX        = "REGEX";
    public static final String ENUM         = "ENUM";
    public static final String DATE         = "DATE";
    public static final String TIMESTAMP    = "TIMESTAMP";
    public static final String EMAIL        = "EMAIL";
    public static final String URL          = "URL";
    public static final String HEXADECIMAL  = "HEXADECIMAL";
    public static final String BASE64       = "BASE64";
    public static final String NAMESPACE    = "NAMESPACE";
    
    private static final Set<String> MULTILINE_TYPES = new HashSet<>();
    private static final Set<String> SINGLELINE_TYPES = new HashSet<>();
    private static final Set<String> ALL_TYPES = new HashSet<>();
    private static final Set<String> VALUES_TYPES = new HashSet<>();
    
    static
    {
    	SINGLELINE_TYPES.add(STRING);
    	SINGLELINE_TYPES.add(NUMBER);
    	SINGLELINE_TYPES.add(BOOLEAN);
    	SINGLELINE_TYPES.add(REGEX);
    	SINGLELINE_TYPES.add(ENUM);
    	SINGLELINE_TYPES.add(DATE);
    	SINGLELINE_TYPES.add(TIMESTAMP);
    	SINGLELINE_TYPES.add(EMAIL);
    	SINGLELINE_TYPES.add(URL);
    	SINGLELINE_TYPES.add(NAMESPACE);
    	
    	MULTILINE_TYPES.add(TEXT);
    	MULTILINE_TYPES.add(BASE64);
    	MULTILINE_TYPES.add(HEXADECIMAL);
    	
    	ALL_TYPES.addAll(SINGLELINE_TYPES);
    	ALL_TYPES.addAll(MULTILINE_TYPES);
    	
    	VALUES_TYPES.add(ENUM);
    	VALUES_TYPES.add(REGEX);
    	VALUES_TYPES.add(NAMESPACE);
    }
    
    public static boolean isValidType(String type)
    {
        return ALL_TYPES.contains(type);
    }
    public static boolean isMultiline(String type)
    {
        return MULTILINE_TYPES.contains(type);
    }
    public static boolean isValuesType(String type)
    {
        return VALUES_TYPES.contains(type);
    }
    public static boolean isValidNamespace(String namespace)
    {
        if (!namespace.endsWith(".stxt")) return false;
        
        try
        {
            new URL("https://" + namespace);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

}
