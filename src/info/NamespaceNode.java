package info;

import java.util.List;

public class NamespaceNode
{
    private String name;
    private String type;
    private List<NamespaceChild> childs;
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public List<NamespaceChild> getChilds()
    {
        return childs;
    }
    public void setChilds(List<NamespaceChild> childs)
    {
        this.childs = childs;
    }
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }    
}
