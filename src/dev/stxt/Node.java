package dev.stxt;

import java.util.ArrayList;
import java.util.List;

import dev.stxt.json.JSONArray;
import dev.stxt.json.JSONObject;

public class Node
{
    private String name;
    private String namespace;
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

    public List<Node> getChildren()
    {
        return children;
    }

    public String getValue()
    {
        return inlineText;
    }

    public List<String> getText()
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

    public JSONObject toJson()
    {
        JSONObject obj = new JSONObject();
        obj.put("name", this.name);
        
        if (this.namespace != null)
            obj.put("namespace", this.namespace);
        if (this.inlineText != null)
            obj.put("value", this.inlineText);
        
        obj.put("line", this.line);
        obj.put("level", this.level);

        // Text array
        JSONArray jText = new JSONArray();
        for (String t : this.multilineText)
        {
            jText.put(t);
        }
        obj.put("text", jText);

        // Children array
        JSONArray jChildren = new JSONArray();
        for (Node child : this.children)
        {
            jChildren.put(child.toJson());
        }
        obj.put("children", jChildren);

        return obj;
    }

    public boolean isMultiline()
    {
        return multiline;
    }
}