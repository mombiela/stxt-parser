package dev.stxt.parser;

import java.io.IOException;

public class STXTProcessor implements Processor
{
    private static final String NAMESPACE = "namespace";
    
    // ----------------------------------
    // Configuration and main constructor
    // ----------------------------------
    
    private final NamespaceRetriever namespaceRetriever;

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
    public void processNodeOnCompletion(Node node) throws ParseException, IOException
    {
        if (isDocRaw(node)) return; // No process
        
        String namespace = (String) node.getMetadata(NAMESPACE);
        NamespaceNode nsNode = namespaceRetriever.getNameSpace(namespace).getNode(node.getName());
        
        NamespaceNodeValidator.validateCount(nsNode, node);
        
        // Validamos nodo
        NamespaceNodeValidator.validateValue(nsNode, node);
        
        // Validamos no implicit multiline
        validateNotImplicitMultiline(node);
    }
    
    @Override
    public void processBeforeAdd(Node parent, Node child) throws IOException, ParseException
    {
        if (isDocRaw(parent)) return; // No process
        
        // Get namespace parent
        String parentNamespace = (String) parent.getMetadata(NAMESPACE);
        String parentName = parent.getName();
        
        NamespaceNode nsNodeParent = namespaceRetriever.getNameSpace(parentNamespace).getNode(parentName);
        
        // Check child name exist
        NamespaceChild nsChild = nsNodeParent.getChilds().get(child.getName());
        if (nsChild == null)
            throw new ParseException("Name not valid: " + child.getName(), child.getLineCreation());

        // Obtenemos el namespace del child
        String namespaceChildString = nsChild.getNamespace();
        if (namespaceChildString == null) namespaceChildString = parentNamespace;
        child.setMetadata(NAMESPACE, namespaceChildString);
        
        // Buscamos namespace
        Namespace namespaceChild = namespaceRetriever.getNameSpace(namespaceChildString);
        if (namespaceChild == null)
            throw new ParseException("Not found namespace " + namespaceChildString, child.getLineCreation());
        
        // Buscamos definición de nodo
        NamespaceNode childNode = namespaceChild.getNode(child.getName());
        if (childNode == null)
            throw new ParseException("Not found " + child.getName() + "in namespace " + namespaceChildString, child.getLineCreation());
        
        // Insertamos según tipo
        child.setMultiline(NamespaceNodeType.isMultiline(childNode.getType()));
    }

    // ------------
    // Main methods
    // ------------
    
    private void updateMainNamespace(Node node) throws IOException, ParseException
    {
        LineSplitter split = LineSplitter.split(node.getName());
        String name = split.centralText;
        String namespace = split.suffix;
        String prefix = split.prefix;
        
        // Raw document: no namespace on root. Do not set any extra metadata; leave as-is
        if (namespace == null) {
            return; // No process
        }
        
        // Validate prefix
        if (prefix != null) throw new ParseException("Prefix not allowed in node name: " + prefix, node.getLineCreation());
        
        // Get namespace
        Namespace ns = namespaceRetriever.getNameSpace(namespace);
        if (ns == null) throw new ParseException("Namespace unknown: " + namespace, node.getLineCreation());
        
        // Check exist name
        NamespaceNode nsNode = ns.getNode(name);
        if (nsNode == null)
            throw new ParseException("Name " + name + " not found in namespace " + namespace, node.getLineCreation());
        
        // Cambiamos nombre
        node.setName(name);
        node.setMetadata(NAMESPACE, namespace);
        
        // Validamos primer nodo
        NamespaceNodeValidator.validateValue(nsNode, node);
    }
    
    private boolean isDocRaw(Node node) {
        // If the node (or its parent) lacks NAMESPACE, treat as raw.
        // We don't traverse parents here; the root sets NAMESPACE on NS docs and children get it in processBeforeAdd.
        return node.getMetadata(NAMESPACE) == null;
    }
    
    private void validateNotImplicitMultiline(Node node) throws ParseException
    {
        if (node.getValues() != null)
        {
            for (NodeLine nl: node.getValues())
            {
                if (nl.isExplicit())
                {
                    throw new ParseException("No allowed explícit multilines in NS documents", nl.getLineCreation());
                }
            }
        }
    }

    @Override
    public void processAfterAdd(Node parent, Node child) throws ParseException, IOException
    {
    }
}