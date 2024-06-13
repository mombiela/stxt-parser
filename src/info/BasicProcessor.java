package info;

import java.util.Set;

public class BasicProcessor implements NodeProcessor
{
    // -------------
    // Configuration
    // -------------
    
    private Set<String> multilineNodes;
    public boolean debug = false; 
    
    public void setMultilineNodes(Set<String> multilineNodes)
    {
        this.multilineNodes = multilineNodes;
    }
    
    // ------------
    // Main methods
    // ------------
    
    @Override
    public void processNodeOnCreation(Node node) throws ParseException
    {
        if (debug) System.out.println(".... Node creation: <" + node.getName() + "> stack: " + node.getLevelCreation());
        
        if (multilineNodes != null && multilineNodes.contains(node.getName()))
            node.setMultiline(true);
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
