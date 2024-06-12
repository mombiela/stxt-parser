package info;

import java.util.ArrayList;
import java.util.List;

public class GrammarProcessor implements NodeProcessor
{
    // -----------------------------
    // Configuration and static vars
    // -----------------------------
    
    private static final boolean debug = true;
    private static final String ROOT_NAMESPACE = "www.semantictext.info/namespace.stxt";
    
    // -----------
    // Main parser
    // -----------
    
    private List<Namespace> namespaces = new ArrayList<>();
    private Namespace currentNamespace = null;
    private NamespaceNode lastNode = null;
    
    public List<Namespace> getNamespaces()
    {
        return namespaces;
    }
    
    @Override
    public void processNodeOnCreation(Node node) throws ParseException 
    {
        if (debug) System.out.println(".... Node creation: <" + node.getName() + "> stack: " + node.getLevelCreation());
    }

    @Override
    public void processNodeOnCompletion(Node node) throws ParseException 
    {
        if (debug) System.out.println(".... Node completion: " + node.getName());
        
        // Node name
        String nodeName = node.getName();
        
        if (node.getLevelCreation()==0)
        {
            TextSplitter nodeNameSplit = TextSplitter.split(nodeName);
            nodeName = nodeNameSplit.getCentralText();
            
            if (!nodeNameSplit.getSuffix().equals(ROOT_NAMESPACE)) 
                throw new ParseException("Namespace not valid: " + nodeNameSplit.getSuffix(), node.getLineCreation());
            
            if (nodeNameSplit.getPrefix()!=null)
                throw new ParseException("Line not valid", node.getLineCreation());
            
            createNameSpace(node, nodeName);
            
            for (Node n: node.getChilds()) updateNamespace(n);
        }
    }

    @Override
    public void processBeforeAdd(Node parent, Node child)
    {
        if (debug) System.out.println(".... Before add " + child.getName() + " to " + parent.getName());
    }

    @Override
    public void processAfterAdd(Node parent, Node child)
    {
        if (debug) System.out.println(".... After add " + child.getName() + " to " + parent.getName());
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
    
    private void updateNamespace(Node node) 
    {
	// TODO Auto-generated method stub
    }
    
    private void updateCreateNode(Node node) throws ParseException
    {
        String name = node.getName().trim();
        
        if (name.length()==0)
        {
            // Nodo de valores (ENUM/REGEX)
            // add to pattern or values node.getValue()
        }
        else
        {
            String type = null;
            String count = null;
            String namespace = null;
            if (node.getValue()!=null)
            {
                TextSplitter nodeParts = TextSplitter.split(node.getValue());
                count = nodeParts.getPrefix();
                type = nodeParts.getCentralText();
                namespace = nodeParts.getSuffix();
            }            
            
            // Nodo normal
            NamespaceNode nsNode = currentNamespace.getNode(name);
            if (nsNode == null)
            {
                nsNode = new NamespaceNode();
                nsNode.setName(name);
                nsNode.setType(type);
                currentNamespace.setNode(name, nsNode);
            }
            else
            {
        	if (type != null) 
        	    throw new ParseException("Type should be defined the first time only", node.getLineCreation());
            }
            
            // Buscamos tipo
            
        }
        
        /*
        
        String name = nodeParts.getCentralText().toLowerCase();
        String type = nodeParts.getSuffix();
        if (type != null) type = type.toUpperCase();
        // Update type
        if (type != null)
        {
            if (nsNode.getType() == null)
            {
                validateType(type, node);
                nsNode.setType(type);
            }
            else
            {
                // check equals
                if (!nsNode.getType().equals(type))
                {
                    throw new ParseException("Type alredy defined: " + nsNode.getType() + " != " + type, node.getLineCreation());
                }
            }
        }
        */
    }

    // -------------------
    // Métodos utilitarios
    // -------------------

    private void validateNamespaceFormat(String namespace, int lineNumber) throws ParseException
    {
        if (!Type.isValidNamespace(namespace)) throw new ParseException("Namespace not valid: " + namespace, lineNumber);
    }
    
    private void validateType(String type, Node node) throws ParseException
    {
        if (!Type.isValidType(type)) 
            throw new ParseException("Type not valid: " + type, node.getLineCreation());
    }

    
}
