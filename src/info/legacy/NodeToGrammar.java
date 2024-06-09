package info.legacy;

import java.util.ArrayList;
import java.util.List;

public class NodeToGrammar
{
    public static List<NamespaceNode> translate(Node node, String namespace) throws ParseException
    {
        // Check definitions
        if (!node.getCanonicalName().equals("ns_def"))
        {
            String error = "Not a namespace definition: " + node.getCanonicalName() + ", " + node.getNamespace();
            throw new ParseException(error);
        }
        
        // Create result
        List<NamespaceNode> result = new ArrayList<NamespaceNode>();
        
        // Get nodes
        List<Node> nodes = node.getChilds();
        for (Node n: nodes)
        {
            NamespaceNode gtype = createGType(n, namespace);
            if (gtype != null) result.add(gtype);
        }
        
        // Return result
        return result;
    }

    private static NamespaceNode createGType(Node node, String namespace) throws ParseException
    {
        // Create result
        NamespaceNode result = new NamespaceNode();
        result.setNamespace(namespace);
        
        // Get attributes of the gtype
        List<Node> childs = node.getChilds();
        List<String> alias = new ArrayList<String>();
        List<NamespaceNodeChild> ccc = new ArrayList<NamespaceNodeChild>();
        for (Node n: childs)
        {
            if (n.getCanonicalName().equals("cn"))    updateName(result, n.getValue().trim());
            if (n.getCanonicalName().equals("a"))     updateAlias(alias, n.getValue().trim());
            if (n.getCanonicalName().equals("type"))  updateType(result, n.getValue().trim());
            if (n.getCanonicalName().equals("ch"))    updateChild(ccc, n, namespace);
        }        
        result.setAlias(alias);
        result.setChilds(ccc);

        return result;
    }

    private static void updateName(NamespaceNode gtype, String value)
    {
        gtype.setName(value);
    }

    private static void updateAlias(List<String> alias, String value)
    {
        alias.add(value);
    }

    private static void updateType(NamespaceNode result, String value) throws ParseException
    {
        try
        {
            result.setNodeType(value.toUpperCase());
        }
        catch (Exception e)
        {
            throw new ParseException("Not valid type: " + value);
        }
    }
    
    private static void updateChild(List<NamespaceNodeChild> ccc, Node n, String namespace)
    {
        // Create child
        NamespaceNodeChild result = new NamespaceNodeChild();
        result.setNamespace(namespace);

        // Traverse nodes and insert
        List<Node> childs = n.getChilds();
        for (Node child: childs)
        {
            if (child.getCanonicalName().equals("n"))         result.setNum(child.getValue().trim());
            if (child.getCanonicalName().equals("ns"))        result.setNamespace(child.getValue().trim());
            if (child.getCanonicalName().equals("cn"))        result.setType(child.getValue().trim());
        }
        
        // Insert into list
        ccc.add(result);
    }

}