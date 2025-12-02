package dev.stxt;

import java.util.ArrayList;
import java.util.List;

public class Node
{
    private String name;
    private String namespace;
    private String type;
    
    private final String value;
    private final List<String> text = new ArrayList<>();
    private final int line;
    private final int level;
    private List<Node> children = new ArrayList<>();

    public Node(int line, int level, String name, String value)
    {
        this.level = level;
        this.line = line;
        this.name = name;
        this.value = value;
    }

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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<Node> getChildren()
    {
        return children;
    }

    public void setChildren(List<Node> children)
    {
        this.children = children;
    }

    public String getValue()
    {
        return value;
    }

    public List<String> getText()
    {
        return text;
    }

    public int getLine()
    {
        return line;
    }

    public int getLevel()
    {
        return level;
    }    
}