package info.semantictext.parser;

import java.util.ArrayList;
import java.util.List;

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
    private String namespace;
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
    
    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
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
    
    public String getTextPrefix()
    {
    	return LineSplitter.split(getText()).getPrefix();
    }
    
    public String getTextSufix()
    {
    	return LineSplitter.split(getText()).getSuffix();
    }
    
    public String getTextCentral()
    {
    	return LineSplitter.split(getText()).getCentralText();
    }    
    
    public String getText()
    {
        StringBuilder result = new StringBuilder();

        if (value != null) {
            result.append(value).append("\n");
        }
        for (NodeLine value: lines) 
        {
            result.append(value.getValue()).append("\n");
        }
        
        return result.toString().replaceAll("(\\s*\\r?\\n)+$", "");
    }
    public String getTextLines()
    {
        StringBuilder result = new StringBuilder();

        for (NodeLine value: lines) 
        {
            result.append(value.getValue()).append("\n");
        }
        
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
    public String getChildText(String cname)
    {
        List<Node> result = getChilds(cname);
        if (result.size() > 1) throw new IllegalArgumentException("More than 1 child. Use getChilds");
        if (result.size() == 0) return null;
        return result.get(0).getText();
    }
    public String getChildTextLines(String cname)
    {
        List<Node> result = getChilds(cname);
        if (result.size() > 1) throw new IllegalArgumentException("More than 1 child. Use getChilds");
        if (result.size() == 0) return null;
        return result.get(0).getTextLines();
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
        result.append("<" + name + "> (line:" + lineCreation + ") namespace=" + (namespace==null?"<NULL>":namespace) + ": '" + getValueShort() + "', lines = " + lines);
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