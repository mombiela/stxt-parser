package dev.stxt.parser;

public class NodeLine
{
    private final String value;
    private final int lineCreation;
    private final int levelCreation;

    public NodeLine(int line, int level, String value, boolean explicit)
    {
        this.levelCreation = level;
        this.lineCreation = line;
        this.value = value;
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

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeValue [value=");
        builder.append(value);
        builder.append(", line=");
        builder.append(lineCreation);
        builder.append("]");
        return builder.toString();
    }    
}
