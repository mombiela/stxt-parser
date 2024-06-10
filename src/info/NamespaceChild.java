package info;

public class NamespaceChild
{
    private String name;
    private String namespace;
    private String num;
    
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
        StringBuilder builder = new StringBuilder();
        builder.append("NamespaceChild [name=");
        builder.append(name);
        builder.append(", num=");
        builder.append(num);
        builder.append(", namespace=");
        builder.append(namespace);
        builder.append("]");
        return builder.toString();
    }    
}
