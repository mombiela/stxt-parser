package info;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    private static final Set<String> MULTILINE_TYPES = new HashSet<>();
    private static final Set<String> SINGLELINE_TYPES = new HashSet<>();
    private static final Set<String> ALL_TYPES = new HashSet<>();
    private static final Set<String> VALUES_TYPES = new HashSet<>();
    private static final Set<String> ALLOWED_COUNT = new HashSet<>();
    
    private static final Pattern COUNT = Pattern.compile("^\\d+(\\+|-)?$");
    
    public static String getDefault()
    {
        return STRING;
    }
    
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
    	
    	MULTILINE_TYPES.add(TEXT);
    	MULTILINE_TYPES.add(BASE64);
    	MULTILINE_TYPES.add(HEXADECIMAL);
    	
    	ALL_TYPES.addAll(SINGLELINE_TYPES);
    	ALL_TYPES.addAll(MULTILINE_TYPES);
    	
    	VALUES_TYPES.add(ENUM);
    	VALUES_TYPES.add(REGEX);
    	
    	ALLOWED_COUNT.add("*");
    	ALLOWED_COUNT.add("+");
    	ALLOWED_COUNT.add("?");
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
    public static boolean isValidCount(String num)
    {
        return ALLOWED_COUNT.contains(num) || validateValue(COUNT, num);
    }
    
    private static boolean validateValue(Pattern pattern, String value)
    {
        Matcher m = pattern.matcher(value);
        return m.matches();
    }    

}
