package info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node
{
    // -------------------------------------
    // Basic properties, getters and setters
    // -------------------------------------

    private String name;
    private String value;
    private List<Node> childs;
    private int lineCreation;
    private Map<String, Object> metadata = new HashMap<String, Object>();
    private boolean multiline;

    public void setMultiline(boolean multiline)
    {
        this.multiline = multiline;
    }

    public Node()
    {
        this.childs = new ArrayList<>();
    }
    
    public boolean isMultiline()
    {
        return multiline;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public Object getMetadata(String key)
    {
        return metadata.get(key);
    }

    public void setMetadata(String key, Object value)
    {
        metadata.put(key, value);
    }
    
    public String getTvalue()
    {
        return value != null ? value.trim() : "";
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public List<Node> getChilds()
    {
        return childs;
    }

    public void addChild(Node child)
    {
        this.childs.add(child);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getLineCreation()
    {
        return lineCreation;
    }

    public void setLineCreation(int lineCreation)
    {
        this.lineCreation = lineCreation;
    }

    // Fast access methods to children
    public List<Node> getChilds(String cname)
    {
        List<Node> result = new ArrayList<Node>();

        for (Node child: childs)
        {
            if (child.getName().equalsIgnoreCase(cname)) result.add(child);
        }

        return result;
    }

    public Node getChild(String cname)
    {
        List<Node> result = getChilds(cname);
        if (result.size() > 1) throw new IllegalArgumentException("More than 1 child. Use getChilds");
        if (result.size() == 0) return null;
        return result.get(0);
    }
    
    // ----------------
    // ToString methods
    // ----------------

    @Override
    public String toString()
    {
        return toString(0);
    }

    private String toString(int level)
    {
        StringBuffer result = new StringBuffer();
        
        for (int i = 0; i<level; i++) result.append("    ");
        result.append("<" + name + " (line:" + lineCreation + ")> <" + getValueShort() + "> " + metadata);
        result.append("\n");
        
        if (childs!=null & childs.size()>0)
        {
            for (int j = 0; j < childs.size(); j++)
            {
                Node c = childs.get(j);
                result.append(c.toString(level+1));
                result.append("\n");
            }
        }
        return result.toString().replaceAll("\n\n", "\n");
    }

    private String getValueShort()
    {
        if (value == null) return "NULL";
        String valueShow = value;
        int i = valueShow.indexOf("\n");
        if (i!=-1) valueShow = valueShow.substring(0, i) + "...";
        if (valueShow.length()>30) valueShow = valueShow.substring(0, 28) + "...";
        return value.length() + " chars: " + valueShow;
    }    
}
