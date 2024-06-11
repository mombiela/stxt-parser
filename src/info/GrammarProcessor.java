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
    public void processNodeOnCreation(Node node, int stackSize) throws ParseException 
    {
        // TODO Auto-generated method stub
        System.out.println(".... Node creation: <" + node.getName() + "> stack: " + stackSize);
        
        if (stackSize == 0)
        {
            // First node
            TextSplitter nameSplit = TextSplitter.split(node.getName());
            if (!NAMESPACE.equals(nameSplit.getSuffix())) 
                throw new ParseException("Namespace is '" + nameSplit.getSuffix() + "' and should be: " + NAMESPACE, node.getLineCreation());
        }
        else
        {
            // Not first node
            
        }
        //node.setMetadata("namespace", NAMESPACE);
    }

    @Override
    public void processNodeOnCompletion(Node node) throws ParseException 
    {
        // TODO Auto-generated method stub
        System.out.println(".... Node completion: " + node.getName());
    }

    @Override
    public void processBeforeAdd(Node parent, Node child)
    {
        // TODO Auto-generated method stub
        System.out.println(".... Before add " + child.getName() + " to " + parent.getName());
    }

    @Override
    public void processAfterAdd(Node parent, Node child)
    {
        // TODO Auto-generated method stub
        System.out.println(".... After add " + child.getName() + " to " + parent.getName());
    }
}
