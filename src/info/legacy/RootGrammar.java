package info.legacy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RootGrammar
{
    public static List<NamespaceNode> generateRootGrammar()
    {
        final String nameSpace = "www.semantictext.info/namespace.stxt";
        
        List<NamespaceNode> result = new ArrayList<NamespaceNode>();
        
        // --------
        // Type def
        // --------
        
        NamespaceNode type = new NamespaceNode();
        type.setName("n_def");
        type.setNodeType(Type.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new ArrayList<>());
        
        List<NamespaceNodeChild> childs = new ArrayList<>();
        childs.add(new NamespaceNodeChild("cn", nameSpace, "1"));
        childs.add(new NamespaceNodeChild("a", nameSpace, "*"));
        childs.add(new NamespaceNodeChild("type", nameSpace, "1"));
        childs.add(new NamespaceNodeChild("dsc", nameSpace, "?"));
        childs.add(new NamespaceNodeChild("ch", nameSpace, "*"));
        type.setChilds(childs);
        
        result.add(type);
        
        // -----
        // Child
        // -----
        
        type = new NamespaceNode();
        type.setName("ch");
        type.setNodeType(Type.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new ArrayList<>());
        
        childs = new ArrayList<>();
        childs.add(new NamespaceNodeChild("cn", nameSpace, "1"));
        childs.add(new NamespaceNodeChild("n", nameSpace, "1"));
        childs.add(new NamespaceNodeChild("ns", nameSpace, "?"));
        type.setChilds(childs);
        
        result.add(type);
        
        // --------------------
        // Namespace definition
        // --------------------
        
        type = new NamespaceNode();
        type.setName("ns_def");
        type.setNodeType(Type.NODE);
        type.setNamespace(nameSpace);
        type.setAlias(new ArrayList<>());
        
        childs = new ArrayList<>();
        childs.add(new NamespaceNodeChild("n_def", nameSpace, "+"));
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
        type.setNodeType(Type.TEXT);
        type.setNamespace(nameSpace);
        type.setAlias(new ArrayList<>());
        result.add(type);
    }

    public static String getRootGrammarContentString() throws IOException
    {
        return UtilsFile.getFileContentFromClasspath("info/old/namespace.stxt");
    }
    
}
