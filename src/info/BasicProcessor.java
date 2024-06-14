package info;

import java.io.IOException;
import java.util.Set;

public class BasicProcessor implements NodeProcessor
{
    // -------------
    // Configuration
    // -------------
    
    private Set<String> multilineNodes;
    private Set<String> allowedNames;
    
    public boolean debug = false; 
    
    public void setMultilineNodes(Set<String> multilineNodes)
    {
        this.multilineNodes = multilineNodes;
    }
    public void setAllowedNames(Set<String> allowedNames)
    {
        this.allowedNames = allowedNames;
    }
    
    // ------------
    // Main methods
    // ------------
    
    @Override
    public void processNodeOnCreation(Node node) throws ParseException, IOException
    {
        if (debug) System.out.println(".... Node creation: <" + node.getName() + "> stack: " + node.getLevelCreation());
        
        if (multilineNodes != null && multilineNodes.contains(node.getName()))
            node.setMultiline(true);
        
        if (allowedNames != null && !allowedNames.contains(node.getName()))
            throw new ParseException("Node name not allowed: " + node.getName(), node.getLineCreation());
    }

    @Override
    public void processNodeOnCompletion(Node node) throws ParseException, IOException 
    {
        if (debug) System.out.println(".... Node completion: " + node.getName());
    }

    @Override
    public void processBeforeAdd(Node parent, Node child) throws ParseException, IOException
    {
        if (debug) System.out.println(".... Before add " + child.getName() + " to " + parent.getName());
    }

    @Override
    public void processAfterAdd(Node parent, Node child) throws ParseException, IOException
    {
        if (debug) System.out.println(".... After add " + child.getName() + " to " + parent.getName());
    }
    
}
