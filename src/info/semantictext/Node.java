package info.semantictext;

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
    private final String value;
    private final List<NodeLine> lines = new ArrayList<>();
    private final int lineCreation;
    private final int levelCreation;
    private List<Node> childs = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<String, Object>();
    private boolean multiline;

    public Node(int line, int level, String name, String value)
    {
        this.levelCreation = level;
        this.lineCreation = line;
        this.name = name;
        this.value = value;
    }
    
    public void setMultiline(boolean multiline)
    {
        this.multiline = multiline;
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

    public void addLine(NodeLine value)
    {
	this.lines.add(value);
    }
    
    public List<NodeLine> getValues()
    {
        return lines;
    }
    
    public String getValuesText()
    {
    	StringBuffer result = new StringBuffer();
    	
    	boolean first = true;
    	for (NodeLine value: lines) 
    	{
    	    if (first) 
    	    {
        		result.append(value.getValue());
        		first = false;
    	    }
    	    else result.append("\n").append(value.getValue());
    	}
	
    	return result.toString();
    }
    
    public String getAllValuesText()
    {
        StringBuilder result = new StringBuilder();

        if (value != null) {
            result.append(value).append("\n");
        }
        
        result.append(getValuesText());
        
        return result.toString().replaceAll("(\\s*\\r?\\n)+$", "");
    }    
    
    // Fast access methods to children
    public List<Node> getChilds(String cname)
    {
        List<Node> result = new ArrayList<Node>();

        for (Node child: childs)
        {
            if (child.getName().equals(cname)) result.add(child);
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
    
    public String getChildValue(String cname)
    {
        List<Node> result = getChilds(cname);
        if (result.size() > 1) throw new IllegalArgumentException("More than 1 child. Use getChilds");
        if (result.size() == 0) return null;
        return result.get(0).getValue();
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
        result.append("<" + name + "> (line:" + lineCreation + ") " + metadata + ": '" + getValueShort() + "', lines = " + lines);
        result.append("\n");
        
        if (childs!=null && childs.size()>0)
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
        if (value == null) return "<NULL>";
        return value;
    }

    public int getLevelCreation()
    {
        return levelCreation;
    }    
}
