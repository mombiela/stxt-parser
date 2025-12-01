package stxt.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamespaceRawTransformer
{
    public static Namespace transformRawNode(Node node) throws ParseException
    {
        Namespace currentNamespace = new Namespace();
        
        // Node name
        String nodeName = node.getName();
        
        // Validation
        LineSplitter nodeNameSplit = LineSplitter.split(nodeName);
        nodeName = nodeNameSplit.centralText;
        
        if (!Constants.NAMESPACE.equalsIgnoreCase(nodeName))
            throw new ParseException("Line not valid: " + nodeName, node.getLineCreation());
        
        if (nodeNameSplit.suffix != null)
            throw new ParseException("Namespace name not allowed in namespace definition: " + nodeNameSplit.suffix, node.getLineCreation());
        
        if (nodeNameSplit.prefix != null)
            throw new ParseException("Line not valid with prefix", node.getLineCreation());

        // Create namespace
        validateNamespaceFormat(node.getValue(), node.getLineCreation());
        currentNamespace = new Namespace();
        currentNamespace.setName(node.getValue());
        
        for (Node n: node.getChilds()) updateNamespace(n, currentNamespace);
        
        // Return namespace
        return currentNamespace;
    }

    // ----
    // Node
    // ----
    
    private static void updateNamespace(Node node, Namespace currentNamespace) throws ParseException 
    {
        String name = node.getName();
        String type = null;
        
        if (node.getValue()!=null)
        {
            LineSplitter nodeParts = LineSplitter.split(node.getValue());
            type = nodeParts.centralText;
        }        
        
        // Nodo normal
        NamespaceNode nsNode = currentNamespace.getNode(name);
        if (nsNode == null)
        {
            nsNode = new NamespaceNode();
            nsNode.setName(name);
            nsNode.setType(type);
            currentNamespace.setNode(name, nsNode);
            if (type != null) validateType(type, node);
            if (type == null) nsNode.setType(NamespaceNodeType.getDefault());
            
            if (node.getValues().size()>0)
            {
        	Set<String> allowedValues = new HashSet<>();
        	for (NodeLine value: node.getValues()) allowedValues.add(value.getValue().trim());
        	
        	if (NamespaceNodeType.isValuesType(type)) 	nsNode.setValues(allowedValues);
        	else                            	throw new ParseException("Type not allow values: " + type, node.getLineCreation());
            }
        }
        else
        {
            if (type != null) 
                throw new ParseException("Type should be defined the first time only", node.getLineCreation());
        }
        
        List<Node> childs = node.getChilds();
        
        if (childs != null)
        {
            if (NamespaceNodeType.isMultiline(type) && childs.size()>0) 
                throw new ParseException("Type " + type + " not allows childs", childs.get(0).getLineCreation());
                
            for (Node child: childs)
            {
                String childName = child.getName();
                if (!childName.isEmpty())
                {
                    // New child
                    NamespaceChild nsChild = new NamespaceChild();
                    nsNode.getChilds().put(childName, nsChild);
                    
                    // Add name
                    nsChild.setName(childName);

                    // Add count
                    String value = child.getValue();
                    
                    if (value != null)
                    {
                        // VALUE/PATTERN
                        LineSplitter split = LineSplitter.split(value);
                        String num = split.prefix;
                        if (num == null) throw new ParseException("Count is requiered", child.getLineCreation());
                        if (!NamespaceNodeType.isValidCount(num)) throw new ParseException("Count is not valid: " + num, child.getLineCreation());
                        String namespace = split.suffix;
                        nsChild.setNum(num != null ? num : "*");
                        nsChild.setNamespace(namespace);
                        if (namespace != null)
                        {
                            if (!NamespaceNodeType.isValidNamespace(namespace))
                                throw new ParseException("Namespace not valid: " + namespace, child.getLineCreation());
                            
                            if (split.centralText!=null)
                                throw new ParseException("Namespace not allow type: " + namespace, child.getLineCreation());
                        }
                    }
                    else
                    {
                    	nsChild.setNum("*");
                    }
                    
                    // process child (only if not contains namespace!)
                    if (nsChild.getNamespace()==null)
                        updateNamespace(child, currentNamespace);
                }
            }
        }
    }
    
    // -------------------
    // Métodos utilitarios
    // -------------------

    private static void validateNamespaceFormat(String namespace, int lineNumber) throws ParseException
    {
        if (!NamespaceNodeType.isValidNamespace(namespace)) throw new ParseException("Namespace not valid: " + namespace, lineNumber);
    }
    
    private static void validateType(String type, Node node) throws ParseException
    {
        if (!NamespaceNodeType.isValidType(type)) 
            throw new ParseException("Type not valid: " + type, node.getLineCreation());
    }
}
