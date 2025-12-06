package dev.stxt;

import java.util.ArrayList;
import java.util.List;

import dev.stxt.utils.JSON;

public class Node
{
    private final String name;
    private final String namespace;
    private final boolean multiline;
    
    private final String inlineText;
    private final List<String> multilineText = new ArrayList<>();
    private final int line;
    private final int level;
    private final List<Node> children = new ArrayList<>();

    public Node(int line, int level, String name, String namespace, boolean multiline, String value)
    {
        this.level = level;
        this.line = line;
        this.name = name;
        this.namespace = namespace;
        this.inlineText = value;
        this.multiline = multiline;
        
        if (!this.inlineText.isEmpty() && this.isMultiline()) throw new IllegalArgumentException("Not empty value with multiline");
    }

    public void addTextLine(String line) 
    {
        this.multilineText.add(line);
    }
    
    public String getName()
    {
        return name;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public List<Node> getChildren()
    {
        return children;
    }

    public String getInlineText()
    {
        return inlineText;
    }

    public List<String> getMultilineText()
    {
        return multilineText;
    }

    public int getLine()
    {
        return line;
    }

    public int getLevel()
    {
        return level;
    }    

	public String toJson() 
	{
		return JSON.toJson(this);
	}

	public String toJsonPretty() 
	{
		return JSON.toJsonPretty(this);
	}

    public boolean isMultiline()
    {
        return multiline;
    }
}