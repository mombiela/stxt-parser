package info;

public class STXTParser extends Parser
{
    private NamespaceRetriever namespaceRetriever;
    
    public STXTParser(NamespaceRetriever namespaceRetriever)
    {
        this.namespaceRetriever = namespaceRetriever;
        addNodeProcessor(new STXTProcessor(namespaceRetriever));
    }    
}

