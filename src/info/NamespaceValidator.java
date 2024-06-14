package info;

import java.util.HashMap;
import java.util.Map;

public class NamespaceValidator
{
    public static void validateValue(NamespaceNode nsNode, Node node) throws ParseException
    {
        if (!isValidValue(nsNode, node))
            throw new ParseException("Value not valid: " + node.getValue(), node.getLineCreation());
    }
    
    public static void validateCount(NamespaceNode nsNode, Node node) throws ParseException
    {
        Map<String, Integer> count = new HashMap<>();
        
        for (Node child: node.getChilds())
        {
            // Count childs
            String childName = child.getName();
            count.put(childName, count.getOrDefault(childName, 0) + 1);
        }
        
        System.out.println("Check node " + node.getName() + " -> " + count);
        
        for (NamespaceChild chNode: nsNode.getChilds().values())
        {
            verifyCount(chNode, count.getOrDefault(chNode.getName(), 0), node);
        }
    }
    
    private static void verifyCount(NamespaceChild chNode, int num, Node node) throws ParseException
    {
        // TODO Auto-generated method stub
        System.out.println("\tVerify " + chNode.getName() + " with count " + chNode.getNum() + " with actual " + num);
        String count = chNode.getNum();
        
        if (count.equals("*")) 
        {
            // OK
            return;
        }
        else if (count.equals("?") && num > 1) 
        {
            throw new ParseException("Node " + node.getName() + " can have only 1 child of type " + chNode.getName(), node.getLineCreation());
        }
    }

    private static boolean isValidValue(NamespaceNode nsNode, Node node)
    {
        return true;
    }
    
}
