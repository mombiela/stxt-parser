package info;

import java.util.HashSet;
import java.util.Set;

public class GrammarProcessor implements NodeProcessor
{
    private static final Set<String> nodes = new HashSet<>();
    private static final Set<String> nodesText = new HashSet<>();
    private static final String NAMESPACE = "www.semantictext.info/namespace.stxt";
    
    static
    {
        // Nodes type NODE
        nodes.add("namespace");
        nodes.add("node");
        nodes.add("child");
        
        // Nodes type TEXT
        nodesText.add("description");
    }
    
    @Override
    public void processNodeOnCreation(Node node) throws ParserException 
    {
        // TODO Auto-generated method stub
        System.out.println(".... check node creation: " + node.getName());
        node.setNamespace(NAMESPACE);
    }

    @Override
    public void processNodeOnCompletion(Node node) throws ParserException 
    {
        // TODO Auto-generated method stub
        System.out.println(".... check node completion: " + node.getName() + " -> " + node.getNamespace());
    }
}
