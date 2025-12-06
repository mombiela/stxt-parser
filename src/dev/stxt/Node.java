package dev.stxt;

import java.util.ArrayList;
import java.util.List;

import dev.stxt.json.JSONArray;
import dev.stxt.json.JSONObject;

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
        obj.put("namespace", this.namespace);
        obj.put("inline_text", this.inlineText);
        obj.put("line", this.line);
        obj.put("level", this.level);

        // Text array
        JSONArray jText = new JSONArray();
        for (String t : this.multilineText)
        {
            jText.put(t);
        }
        obj.put("multiline_text", jText);

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