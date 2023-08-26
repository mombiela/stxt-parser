package info.semantictext.grammar;

import info.semantictext.NamespaceNode;
import info.semantictext.NamespaceNodeChild;
import info.semantictext.NodeType;

import java.util.ArrayList;
import java.util.List;

public class NSGrammar
{
    public static List<NamespaceNode> generateGrammar()
    {
        final String nameSpace = "www.semantictext.info/namespace.stxt";
        
        List<NamespaceNode> result = new ArrayList<NamespaceNode>();
        
        // --------
        // Type def
        // --------
        
        NamespaceNode type = new NamespaceNode();
        type.setName("n_def");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{});
        
        NamespaceNodeChild[] childs = new NamespaceNodeChild[5];
        childs[0] = new NamespaceNodeChild("cn", nameSpace, "1");
        childs[1] = new NamespaceNodeChild("a", nameSpace, "*");
        childs[2] = new NamespaceNodeChild("type", nameSpace, "1");
        childs[3] = new NamespaceNodeChild("dsc", nameSpace, "?");
        childs[4] = new NamespaceNodeChild("ch", nameSpace, "*");
        type.setChilds(childs);
        
        result.add(type);
        
        // -----
        // Child
        // -----
        
        type = new NamespaceNode();
        type.setName("ch");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{});
        
        childs = new NamespaceNodeChild[3];
        childs[0] = new NamespaceNodeChild("cn", nameSpace, "1");
        childs[1] = new NamespaceNodeChild("n", nameSpace, "1");
        childs[2] = new NamespaceNodeChild("ns", nameSpace, "?");
        type.setChilds(childs);
        
        result.add(type);
        
        // --------------------
        // Namespace definition
        // --------------------
        
        type = new NamespaceNode();
        type.setName("ns_def");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{});
        
        childs = new NamespaceNodeChild[1];
        childs[0] = new NamespaceNodeChild("n_def", nameSpace, "+");
        type.setChilds(childs);
        
        result.add(type);
        
        // -------------
        // Simple Childs
        // -------------
        
        createSimple("cn", nameSpace, result);
        createSimple("a", nameSpace, result);
        createSimple("type", nameSpace, result);
        createSimple("n", nameSpace, result);
        createSimple("dsc", nameSpace, result);
        createSimple("ns", nameSpace, result);
        
        return result;
    }
    
    private static void createSimple(String name, String nameSpace, List<NamespaceNode> result)
    {
        NamespaceNode type = new NamespaceNode();
        type.setName(name);
        type.setNodeType(NodeType.TEXT);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{});
        result.add(type);
    }
    
}
