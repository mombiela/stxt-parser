package info;

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
        nodes.add("namespace definition");
        nodes.add("node definition");
        nodes.add("child definition");
        
        nodesText.add("namespace");
        nodesText.add("name");
        nodesText.add("description");
        nodesText.add("count");
        nodesText.add("type");
    }
    
    @Override
    public boolean isNodeText(Node lastNode)
    {
        String name = lastNode.getName().toLowerCase();
        boolean result = nodesText.contains(name); 
        System.out.println("....\tisNodeText: " + lastNode.getName() + " of " + lastNode.getNamespace() + " -> " + result);
        return result;
    }

    @Override
    public String deduceNameSpace(String parentNamespace, String parentName, String childName)
    {
        System.out.println("....\tDeduce nameSpace of " 
                + parentNamespace + ":" + parentName + " -> " + childName + " => " + NAMESPACE);
        return NAMESPACE;
    }

    @Override
    public void validateNode(Node n) throws IOException
    {
        System.out.println("....\tcheck node: " + n.getName() + " -> " + n.getNamespace());
    }
    
    @Override
    public void updateNode(Node n) throws IOException
    {
        System.out.println("....\tupdate node: " + n.getName() + " -> " + n.getNamespace());
    }
}
