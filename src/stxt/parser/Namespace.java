package stxt.parser;

import java.util.LinkedHashMap;
import java.util.Map;

public class Namespace
{
    private Map<String, NamespaceNode> nodes = new LinkedHashMap<String, NamespaceNode>();
    private String name;
    
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

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Namespace: " + name + "\n");
        for (String nodeName: nodes.keySet())
        {
            NamespaceNode node = nodes.get(nodeName);
            builder.append("NODE: " + node.getName() + ", type: " + node.getType() + " -> " + node.getValues() + "\n");
            for (String childName: node.getChilds().keySet())
            {
                NamespaceChild child = node.getChilds().get(childName);
                builder.append("\tChild: " + child + "\n");
            }
        }
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
