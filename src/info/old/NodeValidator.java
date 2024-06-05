package info.old;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeValidator
{
    private static final Pattern P_BINARY       = Pattern.compile("^(0|1|\\s)+$");
    private static final Pattern P_BOOLEAN      = Pattern.compile("^0|1$");
    private static final Pattern P_HEXADECIMAL  = Pattern.compile("^([a-f0-9]|\\s)+$");
    private static final Pattern P_INTEGER      = Pattern.compile("^(\\-|\\+)?\\d+$");
    private static final Pattern P_NATURAL      = Pattern.compile("^\\d+$");
    private static final Pattern P_NUMBER       = Pattern.compile("^(\\-|\\+)?\\d+\\.\\d+(e(\\-|\\+)?\\d+)?$");
    private static final Pattern P_RATIONAL     = Pattern.compile("^(\\-|\\+)?\\d+\\/\\d+$");
    
    public static void validate(Node n, NamespaceNode gtype) throws IOException
    {
        String nodeType = gtype.getNodeType();
        if (Type.BASE64.equals(nodeType))           validateBase64(n);
        else if (Type.BINARY.equals(nodeType))      validateBinary(n);
        else if (Type.BOOLEAN.equals(nodeType))     validateBoolean(n);
        else if (Type.HEXADECIMAL.equals(nodeType)) validateHexadecimal(n);
        else if (Type.INTEGER.equals(nodeType))     validateInteger(n);
        else if (Type.NATURAL.equals(nodeType))     validateNatural(n);
        else if (Type.URL.equals(nodeType))         validateUrl(n);
        else if (Type.NODE.equals(nodeType))        validateNode(n, gtype);
        else if (Type.NUMBER.equals(nodeType))      validateNumber(n);
        else if (Type.RATIONAL.equals(nodeType))    validateRational(n);
        else if (Type.TEXT.equals(nodeType))        validateText(n);
        else throwErrorNode(n, "Node type not supported: " + nodeType);            
    }

    // ------------------------
    // Validation of simple nodes
    // ------------------------
    
    private static void validateBinary(Node n) throws ParseException
    {
        validateValue(n, P_BINARY, "Invalid binary");
    }

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

    private static void validateRational(Node n) throws ParseException
    {
        validateValue(n, P_RATIONAL, "Invalid rational");
    }

    private static void validateBase64(Node n) throws ParseException
    {
        try
        {
            Base64.getDecoder().decode(Utils.cleanupString(n.getValue()));
        }
        catch (Exception e)
        {
            throwErrorNode(n, "Invalid Base64 value: " + n.getValue());
        }
    }
    
    private static void validateText(Node n) throws ParseException
    {
        // No validation, but trim trailing line breaks
    }
    
    private static void validateUrl(Node n) throws ParseException
    {
        if (!isValidURL(n.getTvalue()))
        {
            throwErrorNode(n, "Invalid URL: " + n.getTvalue());
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
        try
        {
            n.setValue(n.getValue().trim());
            String value = n.getValue().toLowerCase(Locale.ENGLISH);
            Matcher m = pattern.matcher(value);
            if (!m.matches()) throwErrorNode(n, error);
        }
        catch (Exception e)
        {
            throwErrorNode(n, error);
        }
    }
    
    // ------------------------
    // Validation of complex node
    // ------------------------
    
    private static void validateNode(Node node, NamespaceNode gtype) throws IOException
    {
        Map<String,Integer> numMap = new HashMap<String, Integer>();
        List<Node> childs = node.getChilds();
        if (childs != null)
        { 
            for (Node child: childs)
            {
                String text = child.getCanonicalName() + ':' + child.getNamespace();
                Integer num = numMap.get(text);
                if (num == null)    numMap.put(text, 1);
                else                numMap.put(text, num+1);
            }
        }
        
        List<NamespaceNodeChild> gtypeChilds = gtype.getChilds();
        for (NamespaceNodeChild ctch: gtypeChilds)
        {
            NamespaceNode g = GrammarFactory.retrieveNamespaceType(ctch.getType(), ctch.getNamespace());
            String text = g.getName() + ':' +  g.getNamespace();
            Integer realNum = numMap.get(text);
            if (realNum == null) realNum = 0;
            
            if (ctch.getNum().equals("*"))
            {
                // Ok
            }
            else if (ctch.getNum().equals("?"))
            {
                if (realNum>1) throwErrorNode(node, "Only one node of type " + g.getName() + " is allowed");
            }
            else if (ctch.getNum().equals("+"))
            {
                if (realNum==0) throwErrorNode(node, "At least one node of type " + g.getName() + " is required");
            }
            else
            {
                int num = Integer.parseInt(ctch.getNum());
                if (realNum != num) throwErrorNode(node, "Node count: " + realNum + ", should be: " + num);
            }
            numMap.remove(text);
        }
        
        if (numMap.size()>0)
        {
            throwErrorNode(node, "Undeclared nodes in the grammar");
        }
    }

    private static void throwErrorNode(Node node, String error) throws ParseException
    {
        String msg = node.getName() + ", " + node.getCanonicalName() + ": " + node.getNamespace() + " -> " + error + "\n" + node;
        throw new ParseException(msg);
    }
}
