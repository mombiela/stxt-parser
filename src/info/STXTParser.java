package info;

public class STXTParser extends Parser
{
    public STXTParser(NamespaceRetriever namespaceRetriever)
    {
        addNodeProcessor(new STXTProcessor(namespaceRetriever));
    }    
}
