package info.semantictext.parser;

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
        type.setName("type_def");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{"Type Definition","Type Name","td"});
        
        GTypeChild[] childs = new GTypeChild[5];
        childs[0] = new GTypeChild("name", nameSpace, "1");
        childs[1] = new GTypeChild("alias", nameSpace, "*");
        childs[2] = new GTypeChild("type", nameSpace, "1");
        childs[3] = new GTypeChild("description", nameSpace, "?");
        childs[4] = new GTypeChild("child", nameSpace, "*");
        type.setChilds(childs);
        
        result.add(type);
        
        // -----
        // Child
        // -----
        
        type = new GType();
        type.setName("child");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{"Child Node","Child Element","ch"});
        
        childs = new GTypeChild[3];
        childs[0] = new GTypeChild("type", nameSpace, "1");
        childs[1] = new GTypeChild("num", nameSpace, "1");
        childs[2] = new GTypeChild("namespace", nameSpace, "?");
        type.setChilds(childs);
        
        result.add(type);
        
        // --------------------
        // Namespace definition
        // --------------------
        
        type = new GType();
        type.setName("namespace_definition");
        type.setNodeType(NodeType.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new String[]{"Namespace Definition","ns_def","ns"});
        
        childs = new GTypeChild[1];
        childs[0] = new GTypeChild("type_def", nameSpace, "+");
        type.setChilds(childs);
        
        result.add(type);
        
        // -------------
        // Simple Childs
        // -------------
        
        createSimple("name", nameSpace, result);
        createSimple("alias", nameSpace, result);
        createSimple("type", nameSpace, result);
        createSimple("num", nameSpace, result);
        createSimple("description", nameSpace, result);
        createSimple("namespace", nameSpace, result);
        
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
