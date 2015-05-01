package info.semantictext.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import info.semantictext.GType;
import info.semantictext.GTypeChild;
import info.semantictext.Node;

public class Validator
{
    private static final Pattern P_BINARY       = Pattern.compile("^[01\\ ]+$");
    private static final Pattern P_BOOLEAN      = Pattern.compile("^0|1$");
    private static final Pattern P_HEXADECIMAL  = Pattern.compile("^[a-f0-9\\ ]+$");
    private static final Pattern P_INTEGER      = Pattern.compile("^(\\-|\\+)?\\d+$");
    private static final Pattern P_NATURAL      = Pattern.compile("^\\d+$");
    private static final Pattern P_NUMBER       = Pattern.compile("^(\\-|\\+)?\\d+\\.\\d+(e(\\-|\\+)?\\d+)?$");
    private static final Pattern P_RATIONAL     = Pattern.compile("^(\\-|\\+)?\\d+\\/\\d+$");
    
    public static void validate(Node n, GType gtype) throws IOException
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
    // Validación nodos simples
    // ------------------------
    
    private static void validateBinary(Node n) throws ParseException
    {
        validateValue(n, P_BINARY, "No es un binario válido");
    }

    private static void validateBoolean(Node n) throws ParseException
    {
        validateValue(n, P_BOOLEAN, "No es un boolean válido");
    }

    private static void validateHexadecimal(Node n) throws ParseException
    {
        validateValue(n, P_HEXADECIMAL, "No es un hexadecimal válido");
    }

    private static void validateInteger(Node n) throws ParseException
    {
        validateValue(n, P_INTEGER, "No es un integer válido");
    }

    private static void validateNatural(Node n) throws ParseException
    {
        validateValue(n, P_NATURAL, "No es un natural válido");
    }

    private static void validateNumber(Node n) throws ParseException
    {
        validateValue(n, P_NUMBER, "No es un number válido");
    }

    private static void validateRational(Node n) throws ParseException
    {
        validateValue(n, P_RATIONAL, "No es un racional válido");
    }

    private static void validateBase64(Node n) throws ParseException
    {
        try
        {
            Base64.decode(n.getValue());
        }
        catch (Exception e)
        {
            throwErrorNode(n, "Valor Base64 no válido.");
        }
    }
    
    private static void validateText(Node n) throws ParseException
    {
        // No validation
    }

    private static void validateValue(Node n, Pattern pattern, String error) throws ParseException
    {
        try
        {
            n.setValue(n.getValue().trim());
            String value = n.getValue().toLowerCase();
            Matcher m = pattern.matcher(value);
            if (!m.matches()) throwErrorNode(n, error);
        }
        catch (Exception e)
        {
            throwErrorNode(n, error);
        }
    }
    
    // ------------------------
    // Validación nodo complejo
    // ------------------------
    
    private static void validateNode(Node node, GType gtype) throws IOException
    {
        // Creamos resumen de nodos hijos
        Map<String,Integer> numMap = new HashMap<String, Integer>();
        List<Node> childs = node.getChilds();
        for (Node child: childs)
        {
            String text = child.getCanonicalName() + ':' + child.getNamespace();
            Integer num = numMap.get(text);
            if (num == null)    numMap.put(text, 1);
            else                numMap.put(text, num+1);
        }
        
        // Recorremos los gtypes, comprobando y eliminando de la lista. Si al acabar no se ha eliminado todo, error!!
        GTypeChild[] gtypeChilds = gtype.getChilds();
        for (GTypeChild ctch: gtypeChilds)
        {
            // Como permitimos alias hay que obtener el canonical
            GType g = GrammarFactory.retrieveGType(ctch.getType(), ctch.getNamespace());
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
                if (realNum==0) throwErrorNode(node, "Debería haber al menos un nodo tipo " + g.getName());
            }
            else
            {
                int num = Integer.parseInt(ctch.getNum());
                if (realNum != num) throwErrorNode(node, "Cantidad nodos: " + realNum + ", debería ser: " + num);
            }
            numMap.remove(text);
        }
        
        // Validamos no vacío
        if (numMap.size()>0)
        {
            throwErrorNode(node, "Existen nodos no definidos en la gramática");
        }
    }

    private static void throwErrorNode(Node node, String error) throws ParseException
    {
        String msg = node.getName() + ", " + node.getCanonicalName() + ": " + node.getNamespace() + " -> " + error + "\n" + node;
        throw new ParseException(msg);
    }
}
