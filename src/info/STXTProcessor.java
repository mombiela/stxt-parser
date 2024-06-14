package info;

import java.io.IOException;

class STXTProcessor extends NodeBasicProcessor
{
    private static final String NAMESPACE = "namespace";
    
    // ----------------------------------
    // Configuration and main constructor
    // ----------------------------------
    
    private NamespaceRetriever namespaceRetriever;

    public STXTProcessor(NamespaceRetriever namespaceRetriever)
    {
        this.namespaceRetriever = namespaceRetriever;
    }

    // ---------------
    // Process methods
    // ---------------
    
    @Override
    public void processNodeOnCreation(Node node) throws ParseException, IOException
    {
        if (debug) System.out.println(".... Node creation: <" + node.getName() + "> stack: " + node.getLevelCreation());
        if (node.getLevelCreation() == 0) updateMainNamespace(node);
    }

    @Override
    public void processBeforeAdd(Node parent, Node child)
    {
        if (debug) System.out.println(".... Before add " + child.getName() + " to " + parent.getName());
    }

    // ------------
    // Main methods
    // ------------
    
    private void updateMainNamespace(Node node) throws IOException, ParseException
    {
        System.out.println("Updating main namespace: " + node.getName());
        LineSplitter split = LineSplitter.split(node.getName());
        String name = split.centralText;
        String namespace = split.suffix;
        
        // Get namespace
        Namespace ns = namespaceRetriever.getNameSpace(namespace);
        if (ns == null) throw new ParseException("Namespace unknown: " + namespace, node.getLineCreation());
        node.setMetadata(NAMESPACE, namespace);
        
        // Check exist name
        if (!ns.getNodes().containsKey(name))
            throw new ParseException("Name " + name + " not found in namespace " + namespace, node.getLineCreation());
    }

}
