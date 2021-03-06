package info.semantictext;

import java.util.Arrays;

public class GType
{
    private String name;
    private String namespace;
    private String[] alias;
    private NodeType nodeType;
    private GTypeChild[] childs;
    
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
    public NodeType getNodeType()
    {
        return nodeType;
    }
    public void setNodeType(NodeType nodeType)
    {
        this.nodeType = nodeType;
    }    
    public GTypeChild[] getChilds()
    {
        return childs;
    }
    public void setChilds(GTypeChild[] childs)
    {
        this.childs = childs;
    }
    
    @Override
    public String toString()
    {
        return "GType [name=" + name + ", namespace=" + namespace + ", nodeType=" + nodeType + ", alias=" + Arrays.toString(alias) + ", childs="
                + Arrays.toString(childs) + "]";
    }    
}
