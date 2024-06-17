package info.semantictext;

public class NodeValue
{
    private final String value;
    private final int lineCreation;
    private final int levelCreation;
    private final boolean explicit;

    public NodeValue(int line, int level, String value, boolean explicit)
    {
        this.levelCreation = level;
        this.lineCreation = line;
        this.value = value;
        this.explicit = explicit;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public int getLineCreation()
    {
        return lineCreation;
    }

    public int getLevelCreation()
    {
        return levelCreation;
    }

    public boolean isExplicit()
    {
        return explicit;
    }
    
    public boolean isImplicit()
    {
        return !explicit;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeValue [value=");
        builder.append(value);
        builder.append(", line=");
        builder.append(lineCreation);
        builder.append(explicit ? ", explicit" : ", implicit");
        builder.append("]");
        return builder.toString();
    }    
}
