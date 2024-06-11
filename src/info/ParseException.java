package info;

public class ParseException extends Exception 
{
    private static final long serialVersionUID = 1L;

    public ParseException(String message, int line) 
    {
        super("Line " + line + ": " + message);
    }
}
