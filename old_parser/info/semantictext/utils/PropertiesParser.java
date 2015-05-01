package info.semantictext.utils;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesParser
{
    private static final Pattern METADATA_PROP = Pattern.compile("(\\$\\{[a-zA-Z_0-9\\.]+\\})");
    
    public static String replace(Properties props, String string)
    {
        Matcher m = METADATA_PROP.matcher(string);
     
        StringBuffer result = new StringBuffer();
        
        int i = 0;
        while (m.find())
        {
            int ini = m.start();
            int fin = m.end();
            
            result.append(string.substring(i, ini));
            
            String prop = m.group();
            prop = prop.substring(2, prop.length()-1);
            prop = props.getProperty(prop, m.group());
            result.append(prop);
            
            i = fin;
        }
        result.append(string.substring(i));
        return result.toString();
    }
}
