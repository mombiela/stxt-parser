package info.semantictext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamespaceProcessor implements Processor
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
            
            if (!Constants.NAMESPACE.equalsIgnoreCase(nodeName))
                throw new ParseException("Line not valid: " + nodeName, node.getLineCreation());
            
            if (nodeNameSplit.suffix != null) 
                throw new ParseException("Namespace name not allowed in namespace definition: " + nodeNameSplit.suffix, node.getLineCreation());
            
            if (nodeNameSplit.prefix != null)
                throw new ParseException("Line not valid with prefix", node.getLineCreation());

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
            if (type == null) nsNode.setType(NamespaceType.getDefault());
            
            if (node.getValues().size()>0)
            {
        	Set<String> allowedValues = new HashSet<>();
        	for (NodeValue value: node.getValues()) allowedValues.add(value.getValue().trim());
        	
        	if (NamespaceType.isValuesType(type)) 	nsNode.setValues(allowedValues);
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
                    
                    if (value != null)
                    {
                        // VALUE/PATTERN
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

    @Override
    public void processNodeOnCreation(Node node) throws ParseException, IOException
    {
    }

    @Override
    public void processBeforeAdd(Node parent, Node child) throws ParseException, IOException
    {
    }

    @Override
    public void processAfterAdd(Node parent, Node child) throws ParseException, IOException
    {
    }    
}
