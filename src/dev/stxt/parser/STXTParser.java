package dev.stxt.parser;

import java.io.File;
import java.io.IOException;

import dev.stxt.parser.ns.Namespace;
import dev.stxt.parser.ns.NamespaceChild;
import dev.stxt.parser.ns.NamespaceNode;
import dev.stxt.parser.ns.NamespaceNodeType;
import dev.stxt.parser.ns.NamespaceNodeValidator;
import dev.stxt.parser.ns.NamespaceRetriever;

public class STXTParser extends Parser
{
    private final NamespaceRetriever namespaceRetriever;
    
    // -------------
    // Constructores
    // -------------
    
    public STXTParser(NamespaceRetriever namespaceRetriever)
    {
        this.namespaceRetriever = namespaceRetriever;
    }    
    
    public STXTParser(File dirNamespaces) throws IOException, ParseException
    {
        NamespaceRetriever namespaceRetriever = new NamespaceRetriever();
        namespaceRetriever.addGrammarDefinitionsFromDir(dirNamespaces);
        
        this.namespaceRetriever = namespaceRetriever;
    }
    
    // ---------------
    // Process methods
    // ---------------
    
    @Override
    protected void processOnCreation(Node node) throws ParseException, IOException
    {
        if (node.getLevelCreation() == 0) updateMainNamespace(node);
    }

    @Override
    protected void processOnCompletion(Node node) throws ParseException, IOException
    {
        if (isRawDoc(node)) return; // No process
        
        String namespace = node.getNamespace();
        NamespaceNode nsNode = namespaceRetriever.getNameSpace(namespace).getNode(node.getName());
        
        NamespaceNodeValidator.validateCount(nsNode, node);
        
        // Validamos nodo
        NamespaceNodeValidator.validateValue(nsNode, node);
    }
    
    @Override
    protected void processBeforeAdd(Node parent, Node child) throws IOException, ParseException
    {
        if (isRawDoc(parent)) return; // No process
        
        // Get namespace parent
        String parentNamespace = parent.getNamespace();
        String parentName = parent.getName();
        
        NamespaceNode nsNodeParent = namespaceRetriever.getNameSpace(parentNamespace).getNode(parentName);
        
        // Check child name exist
        NamespaceChild nsChild = nsNodeParent.getChilds().get(child.getName());
        if (nsChild == null)
            throw new ParseException("Name not valid: " + child.getName(), child.getLineCreation());

        // Obtenemos el namespace del child
        String namespaceChildString = nsChild.getNamespace();
        if (namespaceChildString == null) namespaceChildString = parentNamespace;
        child.setNamespace(namespaceChildString);
        
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
        
        // Raw document: no namespace on root. Do not set any extra field; leave as-is
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
        node.setNamespace(namespace);
        
        // Validamos primer nodo
        NamespaceNodeValidator.validateValue(nsNode, node);
    }
    
    private boolean isRawDoc(Node node) 
    {
        // If the node lacks namespace, treat as raw.
        return node.getNamespace() == null;
    }    
}
