package info.semantictext.grammar;

import info.semantictext.GType;
import info.semantictext.GTypeChild;
import info.semantictext.NodeType;

import java.util.ArrayList;
import java.util.List;

public class NSGrammar
{
    public static List<GType> generateGrammar()
    {
        final String nameSpace = "www.semantictext.info/namespace.stxt";
        
        List<GType> result = new ArrayList<GType>();
        
        // --------
        // Type def
        // --------
        
        GType type = new GType();
        type.setName("n_def");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{});
        
        GTypeChild[] childs = new GTypeChild[5];
        childs[0] = new GTypeChild("cn", nameSpace, "1");
        childs[1] = new GTypeChild("a", nameSpace, "*");
        childs[2] = new GTypeChild("type", nameSpace, "1");
        childs[3] = new GTypeChild("dsc", nameSpace, "?");
        childs[4] = new GTypeChild("ch", nameSpace, "*");
        type.setChilds(childs);
        
        result.add(type);
        
        // -----
        // Child
        // -----
        
        type = new GType();
        type.setName("ch");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{});
        
        childs = new GTypeChild[3];
        childs[0] = new GTypeChild("cn", nameSpace, "1");
        childs[1] = new GTypeChild("n", nameSpace, "1");
        childs[2] = new GTypeChild("ns", nameSpace, "?");
        type.setChilds(childs);
        
        result.add(type);
        
        // --------------------
        // Namespace definition
        // --------------------
        
        type = new GType();
        type.setName("ns_def");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{});
        
        childs = new GTypeChild[1];
        childs[0] = new GTypeChild("n_def", nameSpace, "+");
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
    
    private static void createSimple(String name, String nameSpace, List<GType> result)
    {
        GType type = new GType();
        type.setName(name);
        type.setNodeType(NodeType.TEXT);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{});
        result.add(type);
    }
    
}
