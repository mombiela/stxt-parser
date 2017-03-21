package info.semantictext.grammar;

import info.semantictext.GType;
import info.semantictext.GTypeChild;
import info.semantictext.Node;
import info.semantictext.NodeType;
import info.semantictext.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class NodeToGrammar
{
    public static List<GType> translate(Node node, String namespace) throws ParseException
    {
        // Check definitions
        if (!node.getCanonicalName().equals("ns_def"))
        {
            String error = "No es una definición de namespace: " + node.getCanonicalName() + ", " + node.getNamespace();
            throw new ParseException(error);
        }
        
        // Create result
        List<GType> result = new ArrayList<GType>();
        
        // Get nodes
        List<Node> nodes = node.getChilds();
        for (Node n: nodes)
        {
            GType gtype = createGType(n, namespace);
            if (gtype != null) result.add(gtype);
        }
        
        // Return result
        return result;
    }

    private static GType createGType(Node node, String namespace) throws ParseException
    {
        // Create result
        GType result = new GType();
        result.setNamespace(namespace);
        
        // Obtenemos los atributos del gtype
        List<Node> childs = node.getChilds();
        List<String> alias = new ArrayList<String>();
        List<GTypeChild> ccc = new ArrayList<GTypeChild>();
        for (Node n: childs)
        {
            if (n.getCanonicalName().equals("cn"))    updateName(result, n.getValue().trim());
            if (n.getCanonicalName().equals("a"))     updateAlias(alias, n.getValue().trim());
            if (n.getCanonicalName().equals("type"))  updateType(result, n.getValue().trim());
            if (n.getCanonicalName().equals("ch"))    updateChild(ccc, n, namespace);
        }        
        result.setAlias(createFromListString(alias));
        result.setChilds(createFromListGtype(ccc));

        return result;
    }

    private static void updateName(GType gtype, String value)
    {
        gtype.setName(value);
    }

    private static void updateAlias(List<String> alias, String value)
    {
        alias.add(value);
    }

    private static void updateType(GType result, String value) throws ParseException
    {
        try
        {
            result.setNodeType(NodeType.valueOf(value.toUpperCase()));
        }
        catch (Exception e)
        {
            throw new ParseException("Not valid type: " + value);
        }
    }
    
    private static void updateChild(List<GTypeChild> ccc, Node n, String namespace)
    {
        // Creamos child
        GTypeChild result = new GTypeChild();
        result.setNamespace(namespace);

        // Recorremos nodos insertando
        List<Node> childs = n.getChilds();
        for (Node child: childs)
        {
            if (child.getCanonicalName().equals("n"))         result.setNum(child.getValue().trim());
            if (child.getCanonicalName().equals("ns"))        result.setNamespace(child.getValue().trim());
            if (child.getCanonicalName().equals("cn"))        result.setType(child.getValue().trim());
        }
        
        // Insertamos en lista
        ccc.add(result);
    }
    
    
    // -------------------
    // Métodos utilitarios
    // -------------------
    
    private static String[] createFromListString(List<String> alias)
    {
        String[] result = new String[alias.size()];
        for (int i = 0; i<alias.size(); i++) result[i] = alias.get(i);
        return result;
    }
    
    private static GTypeChild[] createFromListGtype(List<GTypeChild> alias)
    {
        if (alias.size()==0) return null;
        GTypeChild[] result = new GTypeChild[alias.size()];
        for (int i = 0; i<alias.size(); i++) result[i] = alias.get(i);
        return result;
    }

}
