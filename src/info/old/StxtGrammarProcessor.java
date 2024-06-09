package info.old;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StxtGrammarProcessor implements Processor
{
    private static final Set<String> nodes = new HashSet<>();
    private static final Set<String> nodesText = new HashSet<>();
    private static final String NAMESPACE = "www.semantictext.info/namespace.stxt";
    
    static
    {
	// Nodes type NODE
        nodes.add("namespace definition");
        nodes.add("node definition");
        nodes.add("child");
        
        // Nodes type TEXT
        nodesText.add("namespace");
        nodesText.add("name");
        nodesText.add("description");
        nodesText.add("count");
        nodesText.add("type");
    }
    
    @Override
    public boolean isNodeText(Node lastNode) throws ParseException
    {
        String name = lastNode.getName().toLowerCase();
        boolean result = nodesText.contains(name); 
        if (!nodesText.contains(name) && !nodes.contains(name))
        {
            throw new ParseException("Error Line " + lastNode.getLineCreation() + ", Node no valid: " + lastNode.getName());
        }
        System.out.println(".... isNodeText: " + lastNode.getName() + " of " + lastNode.getNamespace() + " -> " + result);
        return result;
    }

    @Override
    public String deduceNameSpace(String parentNamespace, String parentName, String childName)
    {
        System.out.println(".... Deduce nameSpace of " 
                + parentNamespace + ":" + parentName + " -> " + childName + " => " + NAMESPACE);
        return NAMESPACE;
    }

    @Override
    public void validateNode(Node n) throws IOException
    {
        System.out.println(".... check node: " + n.getName() + " -> " + n.getNamespace());
    }
    
    @Override
    public void updateNode(Node n) throws IOException
    {
        System.out.println(".... update node: " + n.getName() + " -> " + n.getNamespace());
    }
}
