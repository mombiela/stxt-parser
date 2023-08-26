package info.semantictext.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Base64;

import info.semantictext.Node;
import info.semantictext.grammar.GrammarFactory;
import info.semantictext.namespace.NamespaceNode;
import info.semantictext.namespace.NamespaceNodeChild;
import info.semantictext.utils.StringCleanup;

public class NodeValidator
{
    private static final Pattern P_BINARY       = Pattern.compile("^(0|1|\\s)+$");
    private static final Pattern P_BOOLEAN      = Pattern.compile("^0|1$");
    private static final Pattern P_HEXADECIMAL  = Pattern.compile("^([a-f0-9]|\\s)+$");
    private static final Pattern P_INTEGER      = Pattern.compile("^(\\-|\\+)?\\d+$");
    private static final Pattern P_NATURAL      = Pattern.compile("^\\d+$");
    private static final Pattern P_NUMBER       = Pattern.compile("^(\\-|\\+)?\\d+\\.\\d+(e(\\-|\\+)?\\d+)?$");
    private static final Pattern P_RATIONAL     = Pattern.compile("^(\\-|\\+)?\\d+\\/\\d+$");
    
    //private static final Pattern TEXT_TRIM      = Pattern.compile("(\\s)*$");
    
    public static void validate(Node n, NamespaceNode gtype) throws IOException
    {
        switch (gtype.getNodeType())
        {
            case BASE64:
                validateBase64(n);
                break;
            case BINARY:
                validateBinary(n);
                break;
            case BOOLEAN:
                validateBoolean(n);
                break;
            case HEXADECIMAL:
                validateHexadecimal(n);
                break;
            case INTEGER:
                validateInteger(n);
                break;
            case NATURAL:
                validateNatural(n);
                break;
            case URL:
                validateUrl(n);
                break;
            case NODE:
                validateNode(n, gtype);
                break;
            case NUMBER:
                validateNumber(n);
                break;
            case RATIONAL:
                validateRational(n);
                break;
            case TEXT:
                validateText(n);
                break;
            default:
                break;
        }
    }

    // ------------------------
    // Validaci�n nodos simples
    // ------------------------
    
    private static void validateBinary(Node n) throws ParseException
    {
        validateValue(n, P_BINARY, "No es un binario v�lido");
    }

    private static void validateBoolean(Node n) throws ParseException
    {
        validateValue(n, P_BOOLEAN, "No es un boolean v�lido");
    }

    private static void validateHexadecimal(Node n) throws ParseException
    {
        validateValue(n, P_HEXADECIMAL, "No es un hexadecimal v�lido");
    }

    private static void validateInteger(Node n) throws ParseException
    {
        validateValue(n, P_INTEGER, "No es un integer v�lido");
    }

    private static void validateNatural(Node n) throws ParseException
    {
        validateValue(n, P_NATURAL, "No es un natural v�lido");
    }

    private static void validateNumber(Node n) throws ParseException
    {
        validateValue(n, P_NUMBER, "No es un number v�lido");
    }

    private static void validateRational(Node n) throws ParseException
    {
        validateValue(n, P_RATIONAL, "No es un racional v�lido");
    }

    private static void validateBase64(Node n) throws ParseException
    {
        try
        {
            Base64.getDecoder().decode(StringCleanup.cleanupString(n.getValue()));
        }
        catch (Exception e)
        {
            throwErrorNode(n, "Valor Base64 no v�lido: " + n.getValue());
        }
    }
    
    private static void validateText(Node n) throws ParseException
    {
        // No validation, but last line trim
        /*
        String value = n.getValue();
        Matcher m = TEXT_TRIM.matcher(value); 
        if (m.find())
        {
            int index = m.start(0);
            value = value.substring(0, index);
            n.setValue(value);
        }
        */
    }
    
    private static void validateUrl(Node n) throws ParseException
    {
        if (!isValidURL(n.getTvalue()))
        {
            throwErrorNode(n, "URL no v�lida: " + n.getTvalue());
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
    // Validaci�n nodo complejo
    // ------------------------
    
    private static void validateNode(Node node, NamespaceNode gtype) throws IOException
    {
        // Creamos resumen de nodos hijos
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
        
        // Recorremos los gtypes, comprobando y eliminando de la lista. Si al acabar no se ha eliminado todo, error!!
        NamespaceNodeChild[] gtypeChilds = gtype.getChilds();
        for (NamespaceNodeChild ctch: gtypeChilds)
        {
            // Como permitimos alias hay que obtener el canonical
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
                if (realNum>1) throwErrorNode(node, "Solo puede haber un nodo del tipo " + g.getName());
            }
            else if (ctch.getNum().equals("+"))
            {
                if (realNum==0) throwErrorNode(node, "Deber�a haber al menos un nodo tipo " + g.getName());
            }
            else
            {
                int num = Integer.parseInt(ctch.getNum());
                if (realNum != num) throwErrorNode(node, "Cantidad nodos: " + realNum + ", deber�a ser: " + num);
            }
            numMap.remove(text);
        }
        
        // Validamos no vac�o
        if (numMap.size()>0)
        {
            throwErrorNode(node, "Existen nodos no definidos en la gram�tica");
        }
    }

    private static void throwErrorNode(Node node, String error) throws ParseException
    {
        String msg = node.getName() + ", " + node.getCanonicalName() + ": " + node.getNamespace() + " -> " + error + "\n" + node;
        throw new ParseException(msg);
    }
}
