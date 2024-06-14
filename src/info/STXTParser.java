package info;

import java.io.IOException;

public class STXTParser extends Parser
{
    public STXTParser(NamespaceRetriever namespaceRetriever)
    {
        addNodeProcessor(new STXTProcessor(namespaceRetriever));
    }    
}

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
        if (node.getLevelCreation() == 0) updateMainNamespace(node);
    }

    @Override
    public void processBeforeAdd(Node parent, Node child) throws IOException, ParseException
    {
        System.out.println("**** " + child.getName() + " -> " + parent.getName());
        
        // Get namespace parent
        String parentNamespace = (String) parent.getMetadata(NAMESPACE);
        String parentName = parent.getName();
        
        NamespaceNode node = namespaceRetriever.getNameSpace(parentNamespace).getNode(parentName);
        
        // Check child name exist
        if (!node.getChilds().containsKey(child.getName()))
            throw new ParseException("Name not valid: " + child.getName(), child.getLineCreation());
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
        
        // Check exist name
        if (!ns.getNodes().containsKey(name))
            throw new ParseException("Name " + name + " not found in namespace " + namespace, node.getLineCreation());
        
        // Cambiamos nombre
        node.setName(name);
        node.setMetadata(NAMESPACE, namespace);
    }
}
