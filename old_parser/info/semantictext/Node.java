package info.semantictext;

import java.util.List;

public class Node
{
    // -------------------------------------
    // Basic properties, getters and setters
    // -------------------------------------
    
    private String name;
    private String canonicalName;
    private String namespace;
    private NodeType type;
    private String value;
    private List<Node> childs;
    private int lineCreation;
    
    public NodeType getType()
    {
        return type;
    }
    public void setType(NodeType type)
    {
        this.type = type;
    }
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }
    public List<Node> getChilds()
    {
        return childs;
    }
    public void setChilds(List<Node> childs)
    {
        this.childs = childs;
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
    public String getCanonicalName()
    {
        return canonicalName;
    }
    public void setCanonicalName(String canonicalName)
    {
        this.canonicalName = canonicalName;
    }
    
    @Override
    public String toString()
    {
        return  toString(1);
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
    
    public String toString(int level)
    {
        StringBuffer result = new StringBuffer();
        result.append("<Node> name=" + name + ", canonicalName=" + canonicalName + ", namespace=" + namespace + ", lineCreation=" + lineCreation + ", type=" + type + ", value=" + value + ", childs=");
        if (childs == null)
        {
            result.append("[]");
        }
        else
        {
            result.append("\n");
            int numNode = 0;
            for (int j = 0; j<childs.size(); j++)
            {
                for (int i = 0; i<level; i++) result.append("\t");
                Node c = childs.get(j);
                result.append("[" + numNode +"]" + c.toString(level+1));
                if (j!=childs.size()-1) result.append("\n");
                numNode++;
            }
        }
        return  result.toString();
    }    
    
    public String toSTXT()
    {
        return toSTXT(1);
    }
    
    private String toSTXT(int level)
    {
        StringBuffer result = new StringBuffer();
        result.append(canonicalName + (level==1? "(" + namespace + ")" : "") + ":");
        if (type != NodeType.NODE)
        {
            result.append(tabulatedText(value, level));
        }
        if (childs != null)
        {
            result.append("\n");
            for (int j = 0; j<childs.size(); j++)
            {
                for (int i = 0; i<level; i++) result.append("\t");
                Node c = childs.get(j);
                result.append(c.toSTXT(level+1));
                if (j!=childs.size()-1) result.append("\n");
            }
        }
        return  result.toString();
    }
    
    private String tabulatedText(String text, int level)
    {
        if (text.indexOf('\n')==-1) return text;
        
        StringBuffer result = new StringBuffer();
        for (int i = 0; i<level; i++) result.append("\t");
        
        text = text.replaceAll("\\n", '\n' + result.toString());
        return text;
    }
    
    public String toSTXTCompact()
    {
        return toSTXTCompact(1);
    }
    
    private String toSTXTCompact(int level)
    {
        StringBuffer result = new StringBuffer();
        result.append(canonicalName + (level==1? "(" + namespace + ")" : "") + ":");
        if (type != NodeType.NODE)
        {
            result.append(compactTabulatedText(value, level));
        }
        if (childs != null)
        {
            result.append("\n");
            for (int j = 0; j<childs.size(); j++)
            {
                result.append(level + ":");
                Node c = childs.get(j);
                result.append(c.toSTXTCompact(level+1));
                if (j!=childs.size()-1) result.append("\n");
            }
        }
        return  result.toString();
    }
    
    private String compactTabulatedText(String text, int level)
    {
        if (text.indexOf('\n')==-1) return text;
        text = text.replaceAll("\\n", '\n' + Integer.toString(level) + ':');
        return text;
    }
}
