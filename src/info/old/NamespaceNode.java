package info.old;

import java.util.List;

public class NamespaceNode
{
    private String name;
    private String namespace;
    private List<String> alias;
    private String nodeType;
    private List<NamespaceNodeChild> childs;
    
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
    public List<String> getAlias()
    {
        return alias;
    }
    public void setAlias(List<String> alias)
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
    public List<NamespaceNodeChild> getChilds()
    {
        return childs;
    }
    public void setChilds(List<NamespaceNodeChild> childs)
    {
        this.childs = childs;
    }
    
    @Override
    public String toString()
    {
        return "NamespaceNode [name=" + name + ", namespace=" + namespace + ", nodeType=" + nodeType + ", alias=" + alias + ", childs=" + childs + "]";
    }    
}
