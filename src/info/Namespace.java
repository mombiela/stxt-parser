package info;

import java.util.LinkedHashMap;
import java.util.Map;

public class Namespace
{
    private Map<String, NamespaceNode> nodes = new LinkedHashMap<String, NamespaceNode>();
    private String name;
    private String description;
    
    public Map<String, NamespaceNode> getNodes()
    {
        return nodes;
    }
    
    public NamespaceNode getNode(String name)
    {
        return nodes.get(name);
    }

    public void setNode(String name, NamespaceNode node)
    {
        nodes.put(name, node);
    }

    public void setNodes(Map<String, NamespaceNode> nodes)
    {
        this.nodes = nodes;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Namespace [name=");
        builder.append(name);
        builder.append(", description=");
        builder.append(description);
        builder.append(", nodes=");
        builder.append(nodes);
        builder.append("]");
        return builder.toString();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }    
}
