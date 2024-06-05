package info;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import info.old.Node;

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
        return nodesText.contains(lastNode.getName().toLowerCase());
    }

    @Override
    public String deduceNameSpace(Node parent, String typeName, int level)
    {
        return NAMESPACE;
    }

    @Override
    public void validateNode(Node n) throws IOException
    {
        System.out.println("... check node: " + n.getCanonicalName() + " -> " + n.getNamespace());
    }
}
