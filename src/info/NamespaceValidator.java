package info;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamespaceValidator
{
    private static final Pattern P_BOOLEAN      = Pattern.compile("^0|1$");
    private static final Pattern P_HEXADECIMAL  = Pattern.compile("^([a-f0-9]|\\s)+$");
    private static final Pattern P_INTEGER      = Pattern.compile("^(\\-|\\+)?\\d+$");
    private static final Pattern P_NATURAL      = Pattern.compile("^\\d+$");
    private static final Pattern P_NUMBER       = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+(e(\\-|\\+)?\\d+)?)?$");
    
    public static void validateCount(NamespaceNode nsNode, Node node) throws ParseException
    {
        Map<String, Integer> count = new HashMap<>();
        
        for (Node child: node.getChilds())
        {
            // Count childs
            String childName = child.getName();
            count.put(childName, count.getOrDefault(childName, 0) + 1);
        }
        
        System.out.println("Check node " + node.getName() + " -> " + count);
        
        for (NamespaceChild chNode: nsNode.getChilds().values())
        {
            verifyCount(chNode, count.getOrDefault(chNode.getName(), 0), node);
        }
    }
    
    private static void verifyCount(NamespaceChild chNode, int num, Node node) throws ParseException
    {
        // TODO Auto-generated method stub
        System.out.println("\tVerify " + chNode.getName() + " with count " + chNode.getNum() + " with actual " + num);
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
        else throw new ParseException("Node type not supported: " + nodeType, n.getLineCreation());            
    }

    // ------------------------
    // Validation of simple nodes
    // ------------------------
    
    private static void validateBoolean(Node n) throws ParseException
    {
        validateValue(n, P_BOOLEAN, "Invalid boolean");
    }

    private static void validateHexadecimal(Node n) throws ParseException
    {
        validateValue(n, P_HEXADECIMAL, "Invalid hexadecimal");
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

    private static void validateBase64(Node n) throws ParseException
    {
        try
        {
            Base64.getDecoder().decode(Utils.cleanupString(n.getValue()));
        }
        catch (Exception e)
        {
            throw new ParseException("Invalid Base64 value: " + n.getValue(), n.getLineCreation());
        }
    }
    
    private static void validateText(Node n) throws ParseException
    {
        // No validation, but trim trailing line breaks
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
