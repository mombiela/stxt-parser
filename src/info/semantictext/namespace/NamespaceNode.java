package info.semantictext.namespace;

import java.util.Arrays;

import info.semantictext.Type;

public class NamespaceNode
{
    private String name;
    private String namespace;
    private String[] alias;
    private Type nodeType;
    private NamespaceNodeChild[] childs;
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getNamespace()
    {
        return namespace;
    }
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }
    public String[] getAlias()
    {
        return alias;
    }
    public void setAlias(String[] alias)
    {
        this.alias = alias;
    }
    public Type getNodeType()
    {
        return nodeType;
    }
    public void setNodeType(Type nodeType)
    {
        this.nodeType = nodeType;
    }    
    public NamespaceNodeChild[] getChilds()
    {
        return childs;
    }
    public void setChilds(NamespaceNodeChild[] childs)
    {
        this.childs = childs;
    }
    
    @Override
    public String toString()
    {
        return "NamespaceNode [name=" + name + ", namespace=" + namespace + ", nodeType=" + nodeType + ", alias=" + Arrays.toString(alias) + ", childs="
                + Arrays.toString(childs) + "]";
    }    
}
