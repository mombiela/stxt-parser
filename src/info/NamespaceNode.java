package info;

import java.util.HashMap;
import java.util.Map;

public class NamespaceNode
{
    private String name;
    private String type;
    private Map<String, NamespaceChild> childs = new HashMap<>();
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Map<String, NamespaceChild> getChilds()
    {
        return childs;
    }
    public void setChild(String name, NamespaceChild child)
    {
        this.childs.put(name, child);
    }
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("NamespaceNode [name=");
        builder.append(name);
        builder.append(", type=");
        builder.append(type);
        builder.append(", childs=");
        builder.append(childs);
        builder.append("]");
        return builder.toString();
    }
}
