package stxt.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NamespaceNode
{
    private String name;
    private String type;
    private Map<String, NamespaceChild> childs = new HashMap<>();
    private Set<String> values = new HashSet<String>();
    
    public Set<String> getValues()
    {
        return values;
    }
    public void setValues(Set<String> values)
    {
        this.values = values;
    }
    public void setChilds(Map<String, NamespaceChild> childs)
    {
        this.childs = childs;
    }
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
        builder.append(", values=");
        builder.append(values);
        builder.append(", childs=");
        builder.append(childs);
        builder.append("]");
        return builder.toString();
    }
}
