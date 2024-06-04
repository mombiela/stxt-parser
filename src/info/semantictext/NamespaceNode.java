package info.semantictext;

import java.util.Arrays;

public class NamespaceNode
{
    private String name;
    private String namespace;
    private String[] alias;
    private String nodeType;
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
    public String getNodeType()
    {
        return nodeType;
    }
    public void setNodeType(String nodeType)
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
