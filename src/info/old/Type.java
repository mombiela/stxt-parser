package info.old;

import java.util.HashSet;
import java.util.Set;

public class Type 
{
    public static final String TREE         	= "TREE";
    public static final String TEXT         	= "TEXT";
    public static final String STRING       	= "STRING";
    public static final String NUMBER       	= "NUMBER";
    public static final String BOOLEAN      	= "BOOLEAN";
    public static final String REGEX          	= "REGEX";
    public static final String ENUM          	= "ENUM";
    public static final String DATE       	= "DATE";
    public static final String TIMESTAMP       	= "TIMESTAMP";
    public static final String EMAIL       	= "EMAIL";
    public static final String URL          	= "URL";
    public static final String HEXADECIMAL	= "HEXADECIMAL";
    public static final String BASE64       	= "BASE64";
    
    private static final Set<String> MULTILINE_TYPES = new HashSet<>();
    private static final Set<String> ALL_TYPES = new HashSet<>();
    
    static
    {
	ALL_TYPES.add(TREE);
	ALL_TYPES.add(TEXT);
	ALL_TYPES.add(STRING);
	ALL_TYPES.add(NUMBER);
	ALL_TYPES.add(BOOLEAN);
	ALL_TYPES.add(REGEX);
	ALL_TYPES.add(ENUM);
	ALL_TYPES.add(DATE);
	ALL_TYPES.add(TIMESTAMP);
	ALL_TYPES.add(EMAIL);
	ALL_TYPES.add(URL);
	ALL_TYPES.add(HEXADECIMAL);
	ALL_TYPES.add(BASE64);
	
	MULTILINE_TYPES.add(TEXT);
	MULTILINE_TYPES.add(BASE64);
	MULTILINE_TYPES.add(HEXADECIMAL);
    }
    
    public static boolean isValidType(String type)
    {
	return ALL_TYPES.contains(type);
    }
    public static boolean isMultiline(String type)
    {
	return MULTILINE_TYPES.contains(type);
    }
}
