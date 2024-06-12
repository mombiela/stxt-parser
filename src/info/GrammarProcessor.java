package info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrammarProcessor implements NodeProcessor
{
    // -----------------------------
    // Configuration and static vars
    // -----------------------------
    
    private static final boolean debug = true;
    private static final String ROOT_NAMESPACE = "www.semantictext.info/namespace.stxt";
    private static final String NAMESPACE   = "namespace";
    private static final String NODE        = "node";
    private static final String CHILD       = "child";
    private static final String PATTERN     = "pattern";
    private static final String VALUE       = "value";
    private static final String DESCRIPTION = "description";
    
    private static final Set<String> NODES = new HashSet<>();
    private static final Set<String> MULTILINE_NODES = new HashSet<>();
    private static final Set<String> ALL_NODES = new HashSet<>();
    
    static
    {
        // Nodes type NODE
        NODES.add(NAMESPACE);
        NODES.add(NODE);
        NODES.add(CHILD);
        NODES.add(PATTERN);
        NODES.add(VALUE);
        
        MULTILINE_NODES.add(DESCRIPTION);
        
        ALL_NODES.addAll(NODES);
        ALL_NODES.addAll(MULTILINE_NODES);
    }
    
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
    public void processNodeOnCreation(Node node, int stackSize) throws ParseException 
    {
        if (debug) System.out.println(".... Node creation: <" + node.getName() + "> stack: " + stackSize);
        
        // Node name
        String nodeName = node.getName().toLowerCase();
        
        // First node
        if (stackSize == 0)
        {
            TextSplitter nameSplit = TextSplitter.split(nodeName);
            
            if (!ROOT_NAMESPACE.equals(nameSplit.getSuffix())) 
                throw new ParseException("Namespace is '" + nameSplit.getSuffix() + "' and should be: " + ROOT_NAMESPACE, node.getLineCreation());
            
            node.setName(nameSplit.getCentralText());
            node.setMetadata(NAMESPACE, ROOT_NAMESPACE);
            
            // Create new namespace
            currentNamespace = new Namespace();
            currentNamespace.setName(node.getValue());
            namespaces.add(currentNamespace);
        }
        
        // Check name
        if (!ALL_NODES.contains(node.getName().toLowerCase()))
        {
            throw new ParseException("Node name not valid: " + node.getName(), node.getLineCreation());
        }
        
        // Node text
        if (node.getName().equalsIgnoreCase("description"))
        {
            node.setMultiline(true);
        }
        //node.setMetadata("namespace", NAMESPACE);
    }

    @Override
    public void processNodeOnCompletion(Node node) throws ParseException 
    {
        if (debug) System.out.println(".... Node completion: " + node.getName());
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
}
