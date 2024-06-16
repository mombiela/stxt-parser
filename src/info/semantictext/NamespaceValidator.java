package info.semantictext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamespaceValidator
{
    private static final Pattern P_BOOLEAN      = Pattern.compile("^(true|false)$");
    private static final Pattern P_HEXADECIMAL  = Pattern.compile("^\\#?([A-Fa-f0-9]|\\s)+$");
    private static final Pattern P_INTEGER      = Pattern.compile("^(\\-|\\+)?\\d+$");
    private static final Pattern P_NATURAL      = Pattern.compile("^\\d+$");
    private static final Pattern P_NUMBER       = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+(e(\\-|\\+)?\\d+)?)?$");
    private static final Pattern P_DATE         = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    
    private static final String ISO_8601_PATTERN =
            "^\\d{4}-\\d{2}-\\d{2}" +               // Date (YYYY-MM-DD)
            "T" +                                   // Time delimiter
            "\\d{2}:\\d{2}(:\\d{2}(\\.\\d{3})?)?" + // Time (HH:mm:ss.sss) with optional seconds and milliseconds
            "(Z|[+-]\\d{2}:\\d{2})?$";              // Time zone (Z or ±HH:mm)

    private static final Pattern P_TIMESTAMP = Pattern.compile(ISO_8601_PATTERN);    
    
    private static final String EMAIL_PATTERN =
            "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?=.{1,64}@.{1,63}\\..{1,63}$)[A-Za-z0-9!#$%&'*+/=?^_`{|}~.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final Pattern P_EMAIL = Pattern.compile(EMAIL_PATTERN);    
    
    public static void validateValue(NamespaceNode nsNode, Node n) throws IOException, ParseException
    {
        String nodeType = nsNode.getType();
        if (NamespaceType.BASE64.equals(nodeType))                  validateBase64(n);
        else if (NamespaceType.BOOLEAN.equals(nodeType))            validateBoolean(n);
        else if (NamespaceType.HEXADECIMAL.equals(nodeType))        validateHexadecimal(n);
        else if (NamespaceType.INTEGER.equals(nodeType))            validateInteger(n);
        else if (NamespaceType.NATURAL.equals(nodeType))            validateNatural(n);
        else if (NamespaceType.URL.equals(nodeType))                validateUrl(n);
        else if (NamespaceType.NUMBER.equals(nodeType))             validateNumber(n);
        else if (NamespaceType.TEXT.equals(nodeType))               validateText(n);
        else if (NamespaceType.STRING.equals(nodeType))             validateText(n);
        else if (NamespaceType.DATE.equals(nodeType))               validateDate(n);
        else if (NamespaceType.EMAIL.equals(nodeType))              validateEmail(n);
        else if (NamespaceType.EMPTY.equals(nodeType))              validateEmtpy(n);
        else if (NamespaceType.TIMESTAMP.equals(nodeType))          validateTimestamp(n);
        else if (NamespaceType.ENUM.equals(nodeType))               validateEnum(n, nsNode.getValues());
        else if (NamespaceType.REGEX.equals(nodeType))              validateRegex(n, nsNode.getValues());
        else throw new ParseException("Node type not supported: " + nodeType, n.getLineCreation());            
    }

    public static void validateCount(NamespaceNode nsNode, Node node) throws ParseException
    {
        Map<String, Integer> count = new HashMap<>();
        
        for (Node child: node.getChilds())
        {
            // Count childs
            String childName = child.getName();
            count.put(childName, count.getOrDefault(childName, 0) + 1);
        }
        
        for (NamespaceChild chNode: nsNode.getChilds().values())
        {
            verifyCount(chNode, count.getOrDefault(chNode.getName(), 0), node);
        }
    }
    
    private static void verifyCount(NamespaceChild chNode, int num, Node node) throws ParseException
    {
        String count = chNode.getNum();
        
        if (count.equals("*")) 
        {
            // OK
            return;
        }
        else if (count.equals("?")) 
        {
            if (num > 1)
                throw new ParseException("Node '" + node.getName() + "' can have only 1 child of name '" 
                        + chNode.getName() + "' and have " + num, node.getLineCreation());
        }
        else if (count.equals("+"))
        {
            if (num == 0)
                throw new ParseException("Node '" + node.getName() + "' should have at least 1 child of name '" 
                        + chNode.getName() + "'", node.getLineCreation());
        }
        else if (count.endsWith("+"))
        {
            int expectedNum = Integer.parseInt(count.substring(0, count.length()-1));
            if (num < expectedNum)
                throw new ParseException("Node '" + node.getName() + "' should have at least " 
                        + expectedNum + " childs of name '" + chNode.getName() + "', and have " + num, node.getLineCreation());
        }
        else if (count.endsWith("-"))
        {
            int expectedNum = Integer.parseInt(count.substring(0, count.length()-1));
            if (num > expectedNum)
                throw new ParseException("Node '" + node.getName() + "' should have maximum of " 
                        + expectedNum + " childs of name '" + chNode.getName() + "', and have " + num, node.getLineCreation());
        }
        else
        {
            int expectedNum = Integer.parseInt(count);
            if (expectedNum != num)
                throw new ParseException("Node '" + node.getName() + "' should have " 
                        + expectedNum + " of child of name '" + chNode.getName() + "', and have " + num, node.getLineCreation());
        }
    }

    // ------------------------
    // Validation of simple nodes
    // ------------------------
    
    private static void validateBoolean(Node n) throws ParseException
    {
        validateValue(n, P_BOOLEAN, "Invalid boolean");
    }

    private static void validateDate(Node n) throws ParseException
    {
        validateValue(n, P_DATE, "Invalid date");
    }

    private static void validateTimestamp(Node n) throws ParseException
    {
        validateValue(n, P_TIMESTAMP, "Invalid timestamp");
    }

    private static void validateEmail(Node n) throws ParseException
    {
        validateValue(n, P_EMAIL, "Invalid email");
    }

    private static void validateInteger(Node n) throws ParseException
    {
        validateValue(n, P_INTEGER, "Invalid integer");
    }

    private static void validateNatural(Node n) throws ParseException
    {
        validateValue(n, P_NATURAL, "Invalid natural");
    }

    private static void validateNumber(Node n) throws ParseException
    {
        validateValue(n, P_NUMBER, "Invalid number");
    }

    private static void validateEmtpy(Node n) throws ParseException
    {
	if (n.getValue()!=null || (n.getValues() != null && n.getValues().size()>0))
	{
	    throw new ParseException("Node '" + n.getName() + "' has to be empty", n.getLineCreation());
	}
    }
    
    private static void validateBase64(Node n) throws ParseException
    {
        try
        {
            Base64.getDecoder().decode(Utils.cleanupString(n.getAllValuesText()));
        }
        catch (Exception e)
        {
            throw new ParseException("Node '" + n.getName() + "' Invalid Base64", n.getLineCreation());
        }
    }
    
    private static void validateHexadecimal(Node n) throws ParseException
    {
	String hex = Utils.cleanupString(n.getAllValuesText());
	Matcher m = P_HEXADECIMAL.matcher(hex);
	if (!m.matches()) throw new ParseException("Node '" + n.getName() + "' Invalid hexadecimal", n.getLineCreation());
    }
    
    
    private static void validateText(Node n) throws ParseException
    {
        // No validation, but trim trailing line breaks
    }
    
    private static void validateEnum(Node n, Set<String> values) throws ParseException
    {
        if (!values.contains(n.getValue()))
            throw new ParseException("Node '" + n.getName() + "' has value not allowed: " + n.getValue(), n.getLineCreation());
    }
    
    private static void validateRegex(Node n, Set<String> values) throws ParseException
    {
        for (String value: values)
        {
            Pattern p = Pattern.compile(value);
            if (p.matcher(n.getValue()).matches()) return;
        }
        throw new ParseException("Node '" + n.getName() + "' has value not allowed: " + n.getValue(), n.getLineCreation());
    }
    
    private static void validateUrl(Node n) throws ParseException
    {
        if (!isValidURL(n.getValue()))
        {
            throw new ParseException("Invalid URL: " + n.getValue(), n.getLineCreation());
        }
    }

    private static boolean isValidURL(String url)
    {
        URL u = null;
        try
        {
            u = new URL(url);
        }
        catch (MalformedURLException e)
        {
            return false;
        }

        try
        {
            u.toURI();
        }
        catch (URISyntaxException e)
        {
            return false;
        }

        return true;
    }

    private static void validateValue(Node n, Pattern pattern, String error) throws ParseException
    {
        Matcher m = pattern.matcher(n.getValue());
        if (!m.matches()) throw new ParseException(n.getName() + ": " + error + " (" + n.getValue() + ")", n.getLineCreation());
    }    
}
