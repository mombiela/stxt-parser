package info.semantictext;

public class ParseException extends Exception 
{
    private static final long serialVersionUID = 1L;

    public int line;
    
    public ParseException(String message, int line) 
    {
        super("Line " + line + ": " + message);
        this.line = line;
    }
}
