package info;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GrammarProcessor implements Processor
{
    private static final Set<String> nodes = new HashSet<>();
    private static final Set<String> nodesText = new HashSet<>();
    private static final String NAMESPACE = "www.semantictext.info/namespace.stxt";
    
    static
    {
        nodes.add("namespace definition");
        nodesText.add("description");
    }
    
    @Override
    public boolean isNodeText(Node lastNode)
    {
        boolean result = nodesText.contains(lastNode.getName().toLowerCase()); 
        System.out.println("....\tisNodeText: " + lastNode.getName() + " of " + lastNode.getNamespace() + " -> " + result);
        return result;
    }

    @Override
    public String deduceNameSpace(Node parent, String typeName, int level)
    {
        System.out.println("....\tDeduce nameSpace of " + typeName + " -> " + NAMESPACE);
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
        n.setCanonicalName(n.getName()); // TODO Hacer correctamente
    }
}
