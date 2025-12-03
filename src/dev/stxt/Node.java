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
    
    private final String value;
    private final List<String> text = new ArrayList<>();
    private final int line;
    private final int level;
    private final List<Node> children = new ArrayList<>();

    public Node(int line, int level, String name, String value, boolean multiline)
    {
        this.level = level;
        this.line = line;
        this.name = name;
        this.value = value;
        this.multiline = multiline;
    }

    public void addTextLine(String line) 
    {
        this.text.add(line);
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

    public JSONObject toJson()
    {
        JSONObject obj = new JSONObject();
        obj.put("name", this.name);
        
        if (this.namespace != null)
            obj.put("namespace", this.namespace);
        if (this.value != null)
            obj.put("value", this.value);
        
        obj.put("line", this.line);
        obj.put("level", this.level);

        // Text array
        JSONArray jText = new JSONArray();
        for (String t : this.text)
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