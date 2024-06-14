package info;

public class NamespaceValidator
{
    public static void validateValue(NamespaceNode nsNode, Node node) throws ParseException
    {
        if (!isValidValue(nsNode, node))
            throw new ParseException("Value not valid: " + node.getValue(), node.getLineCreation());
    }
    
    public static void validateCount(NamespaceNode nsNode, Node node) throws ParseException
    {
    }
    
    private static boolean isValidValue(NamespaceNode nsNode, Node node)
    {
        return true;
    }
    
}
