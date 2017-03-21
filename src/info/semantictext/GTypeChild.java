package info.semantictext;

public class GTypeChild
{
    private String type;
    private String namespace;
    private String num;
    
    public GTypeChild()
    {
    }
    
    public GTypeChild(String type, String namespace, String num)
    {
        this.type = type;
        this.namespace = namespace;
        this.num = num;
    }
    
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public String getNamespace()
    {
        return namespace;
    }
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }
    public String getNum()
    {
        return num;
    }
    public void setNum(String num)
    {
        this.num = num;
    }
    
    @Override
    public String toString()
    {
        return "GTypeChild [type=" + type + ", namespace=" + namespace + ", num=" + num + "]";
    }   
}
