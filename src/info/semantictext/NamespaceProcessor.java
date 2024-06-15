package info.semantictext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamespaceProcessor extends BasicProcessor
{
    // -----------
    // Main parser
    // -----------
    
    private List<Namespace> namespaces = new ArrayList<>();
    private Namespace currentNamespace = null;
    
    public List<Namespace> getNamespaces()
    {
        return namespaces;
    }
    
    @Override
    public void processNodeOnCompletion(Node node) throws ParseException 
    {
        // Node name
        String nodeName = node.getName();
        
        if (node.getLevelCreation()==0)
        {
            // Validation
            LineSplitter nodeNameSplit = LineSplitter.split(nodeName);
            nodeName = nodeNameSplit.centralText;
            
            if (!Constants.ROOT_NAMESPACE.contentEquals(nodeNameSplit.suffix)) 
                throw new ParseException("Namespace not valid: " + nodeNameSplit.suffix, node.getLineCreation());
            
            if (nodeNameSplit.prefix != null)
                throw new ParseException("Line not valid", node.getLineCreation());

            // Create namespace
            createNameSpace(node, nodeName);
            for (Node n: node.getChilds()) updateNamespace(n);
        }
    }

    // ---------
    // Namespace
    // ---------
    
    private void createNameSpace(Node node, String nodeName) throws ParseException
    {
        if (node.getLevelCreation() != 0) 
            throw new ParseException("Namespace not in valid position: " + node.getLevelCreation(), node.getLineCreation());
        
        // Create new namespace
        validateNamespaceFormat(node.getValue(), node.getLineCreation());
        currentNamespace = new Namespace();
        currentNamespace.setName(node.getValue());
        namespaces.add(currentNamespace);
    }

    // ----
    // Node
    // ----
    
    private void updateNamespace(Node node) throws ParseException 
    {
        String name = node.getName();
        String type = null;
        final String nodeValue = node.getValue();
        Set<String> values = null;
        
        if (nodeValue!=null)
        {
            // VALUE/PATTERN
            String[] nodeValueParts = nodeValue.split("\\n");
            if (nodeValueParts.length > 1)
            {
        	type = nodeValueParts[0];
        	values = new HashSet<>();
        	for (int i = 1; i<nodeValueParts.length; i++) values.add(nodeValueParts[i].trim());
            }
            
            LineSplitter nodeParts = LineSplitter.split(nodeValueParts[0]);
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
            if (type == null) nsNode.setType(NamespaceType.getDefault());
            
            if (values != null)
            {
        	if (NamespaceType.isValuesType(type)) 	nsNode.setValues(values);
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
            if (NamespaceType.isMultiline(type) && childs.size()>0) 
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
                    System.out.println("value = '" + value + "'");
                    
                    if (value != null)
                    {
                	
                        // VALUE/PATTERN
                        String[] valueParts = value.split("\\n");
                        if (valueParts.length > 1) value = valueParts[0];
                	
                        LineSplitter split = LineSplitter.split(value);
                        String num = split.prefix;
                        if (num == null) throw new ParseException("Count is requiered", child.getLineCreation());
                        if (!NamespaceType.isValidCount(num)) throw new ParseException("Count is not valid: " + num, child.getLineCreation());
                        String namespace = split.suffix;
                        nsChild.setNum(num != null ? num : "*");
                        nsChild.setNamespace(namespace);
                        if (namespace != null)
                        {
                            if (!NamespaceType.isValidNamespace(namespace))
                                throw new ParseException("Namespace not valid: " + namespace, child.getLineCreation());
                            
                            if (split.centralText!=null)
                                throw new ParseException("Namespace not allow type: " + namespace, child.getLineCreation());
                        }
                    }
                    
                    // process child (only if not contains namespace!)
                    if (nsChild.getNamespace()==null)
                        updateNamespace(child);
                }
            }
        }
    }
    
    // -------------------
    // Métodos utilitarios
    // -------------------

    private void validateNamespaceFormat(String namespace, int lineNumber) throws ParseException
    {
        if (!NamespaceType.isValidNamespace(namespace)) throw new ParseException("Namespace not valid: " + namespace, lineNumber);
    }
    
    private void validateType(String type, Node node) throws ParseException
    {
        if (!NamespaceType.isValidType(type)) 
            throw new ParseException("Type not valid: " + type, node.getLineCreation());
    }    
}
