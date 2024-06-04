package info.semantictext;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarValidator
{
    // Iterate through nodes and validate
    public static void validate(Map<String, NamespaceNode> grammar, String namespace) throws IOException
    {
        Collection<NamespaceNode> types = grammar.values();
        for (NamespaceNode type: types)
        {
            validate(grammar, namespace, type);
        }
    }

    public static void validate(List<NamespaceNode> grammar) throws ParseException
    {
        Set<String> nsDef = new HashSet<String>();
        
        for (NamespaceNode type: grammar)
        {
            // Check name
            String canonicalName = Utils.uniform(type.getName());
            if (nsDef.contains(canonicalName))
                throw new ParseException("Duplicated name: " + canonicalName + " -> " + type.getNamespace());
            
            // Check alias
            String[] alias = type.getAlias();
            if (alias != null)
            {
                for(String a: alias)
                {
                    // Check alias
                    a = Utils.uniform(a);
                    if (nsDef.contains(a))
                        throw new ParseException("Duplicated alias: " + a + " -> " + type.getNamespace());
                }
            }
        }
    }
    
    private static void validate(Map<String, NamespaceNode> grammar, String namespace, NamespaceNode type) throws IOException
    {
        // Validate that it's a correct definition
        if (type.getName() == null || type.getName().trim().length() == 0)
        {
            throw new ParseException("Incorrect grammar definition: " + namespace + ", the node doesn't have a defined name: " + type);
        }
        if (type.getNamespace() == null || type.getNamespace().trim().length() == 0)
        {
            throw new ParseException("Incorrect grammar definition: " + namespace + ", the node doesn't have a defined namespace: " + type);
        }
        if (type.getNodeType() == null)
        {
            throw new ParseException("Incorrect grammar definition: " + namespace + ", the node doesn't have a defined node type: " + type);
        }
        
        // Validate that a non-node type doesn't have child nodes
        if (!Type.NODE.equals(type.getNodeType()))
        {
            if (type.getChilds() != null && type.getChilds().length != 0)
            {
                throw new ParseException("Incorrect grammar definition: " + namespace + ", the node cannot have children: " + type);
            }
            return;
        }
        
        // It's a NODE node, so we need to validate that it has child nodes
        NamespaceNodeChild[] childs = type.getChilds();
        if (childs == null || childs.length == 0)
        {
            throw new ParseException("Incorrect grammar definition: " + namespace + ", the node must have children: " + type);
        }
        
        // Validate names and aliases of children
        Set<String> childsAlias = new HashSet<String>();
        for (NamespaceNodeChild child: childs)
        {
            // Get type
            NamespaceNode g = getGType(child, grammar, namespace);
            if (g == null)
            {
                throw new ParseException("Incorrect grammar definition: " 
                        + namespace + ", the node doesn't have a valid child: " + type + " child= " + child);
            }
            
            // Validate names and aliases
            // Check name
            String canonicalName = Utils.uniform(type.getName());
            if (childsAlias.contains(canonicalName))
                throw new ParseException("Duplicated name or childName: " + canonicalName + " -> " + type);
            
            // Check alias
            String[] alias = type.getAlias();
            if (alias != null)
            {
                for(String a: alias)
                {
                    // Check alias
                    a = Utils.uniform(a);
                    if (childsAlias.contains(a))
                        throw new ParseException("Duplicated name or childName: " + canonicalName + " -> " + type);
                }
            }
        }
    }

    private static NamespaceNode getGType(NamespaceNodeChild child, Map<String, NamespaceNode> grammar, String namespace) throws IOException
    {
        if (child.getNamespace().equalsIgnoreCase(namespace))
        {
            return grammar.get(Utils.uniform(child.getType()));
        }
        else
        {
            return GrammarFactory.retrieveNamespaceType(child.getType(), child.getNamespace());
        }
    }
}
