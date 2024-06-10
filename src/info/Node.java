package info;

import java.util.ArrayList;
import java.util.List;

public class Node
{
    // -------------------------------------
    // Basic properties, getters and setters
    // -------------------------------------

    private String name;
    private String namespace;
    private String type = Type.STRING;
    private String value;
    private List<Node> childs;
    private int lineCreation;

    public Node()
    {
        this.childs = new ArrayList<>();
    }
    
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getValue()
    {
        return value;
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

    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public int getLineCreation()
    {
        return lineCreation;
    }

    public void setLineCreation(int lineCreation)
    {
        this.lineCreation = lineCreation;
    }

    // ----------------
    // ToString methods
    // ----------------

    @Override
    public String toString()
    {
        return toString(1);
    }

    private String toString(int level)
    {
        StringBuffer result = new StringBuffer();
        result.append("<Node> name=" + name + ", namespace=" + namespace + ", lineCreation=" + lineCreation + ", type=" + type + ", value=" + value + ", childs=");
        if (childs == null)
        {
            result.append("[]");
        }
        else
        {
            result.append("\n");
            int numNode = 0;
            for (int j = 0; j < childs.size(); j++)
            {
                for (int i = 0; i < level; i++) result.append("\t");
                Node c = childs.get(j);
                result.append("[" + numNode + "]" + c.toString(level + 1));
                if (j != childs.size() - 1) result.append("\n");
                numNode++;
            }
        }
        return result.toString();
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
    
    public boolean isMultiline()
    {
	return Type.isMultiline(type);
    }
    public boolean isValidType()
    {
	return Type.isValidType(type);
    }
}
