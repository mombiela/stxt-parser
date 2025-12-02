package dev.stxt;

import java.util.HashSet;
import java.util.Set;

public class NodeType 
{
    public static final String TEXT         = "TEXT";
    public static final String STRING       = "STRING";
    public static final String NUMBER       = "NUMBER";
    public static final String BOOLEAN      = "BOOLEAN";
    
    private static final Set<String> MULTILINE_TYPES = new HashSet<>();
    private static final Set<String> SINGLELINE_TYPES = new HashSet<>();
    private static final Set<String> ALL_TYPES = new HashSet<>();
    
    public static String getDefault()
    {
        return STRING;
    }
    
    static
    {
    	SINGLELINE_TYPES.add(STRING);
    	SINGLELINE_TYPES.add(NUMBER);
    	SINGLELINE_TYPES.add(BOOLEAN);
    	
    	MULTILINE_TYPES.add(TEXT);
        ALL_TYPES.addAll(SINGLELINE_TYPES);
        ALL_TYPES.addAll(MULTILINE_TYPES);
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
