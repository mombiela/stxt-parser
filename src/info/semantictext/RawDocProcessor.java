package info.semantictext;

import java.io.IOException;
import java.util.Set;

public class RawDocProcessor implements Processor
{
    // -------------
    // Configuration
    // -------------
    
    private Set<String> multilineNodes;
    private Set<String> allowedNames;
    
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
        if (multilineNodes != null && multilineNodes.contains(node.getName()))
            node.setMultiline(true);
        
        if (allowedNames != null && !allowedNames.contains(node.getName()))
            throw new ParseException("Node name not allowed: " + node.getName(), node.getLineCreation());
    }

    @Override
    public void processNodeOnCompletion(Node node) throws ParseException, IOException 
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
