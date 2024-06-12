package info;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.org.apache.xml.internal.serialize.LineSeparator;

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
    public void processNodeOnCreation(Node node) throws ParseException 
    {
        if (debug) System.out.println(".... Node creation: <" + node.getName() + "> stack: " + node.getLevelCreation());
        
        // Node name
        String nodeName = node.getName().toLowerCase();
        
        // First node special
        if (node.getLevelCreation() == 0)
        {
            TextSplitter nodeNameSplit = TextSplitter.split(nodeName);
            nodeName = nodeNameSplit.getCentralText();
            if (!nodeNameSplit.getSuffix().equals(ROOT_NAMESPACE)) 
                throw new ParseException("Namespace not valid: " + nodeNameSplit.getSuffix(), node.getLineCreation());
            
            if (nodeNameSplit.getPrefix()!=null)
                throw new ParseException("Line not valid", node.getLineCreation());
        }
        
        // Check name
        if (!ALL_NODES.contains(nodeName))
            throw new ParseException("Node name not valid: " + node.getName(), node.getLineCreation());
        
        // Multiline
        if (MULTILINE_NODES.contains(nodeName)) 
            node.setMultiline(true);
        
        // Check name
        if (nodeName.equals(NAMESPACE)) createNameSpace(node, nodeName);
        else if (nodeName.equals(NODE)) updateCreateNode(node);
        
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
    
    private void updateCreateNode(Node node) throws ParseException
    {
        if (node.getLevelCreation() != 1) 
            throw new ParseException("Node not in valid position: " + node.getLevelCreation(), node.getLineCreation());
        
        TextSplitter nodeParts = TextSplitter.split(node.getValue());
        
        String name = nodeParts.getCentralText().toLowerCase();
        NamespaceNode nsNode = currentNamespace.getNode(name);
        if (nsNode == null)
        {
            nsNode = new NamespaceNode();
            nsNode.setName(name);
            currentNamespace.setNode(name, nsNode);
        }
    }

    // -------------------
    // Métodos utilitarios
    // -------------------

    private void validateNamespaceFormat(String namespace, int lineNumber) throws ParseException
    {
        if (!isValidNamespace(namespace)) throw new ParseException("Namespace not valid: " + namespace, lineNumber);
    }
    
    private boolean isValidNamespace(String namespace)
    {
        if (!namespace.endsWith(".stxt")) return false;
        
        try
        {
            new URL("https://" + namespace);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
}
