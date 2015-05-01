package info.semantictext.parser;

import java.io.IOException;

public class ParseException extends IOException
{
    public ParseException()
    {
        super();
    }

    public ParseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ParseException(String message)
    {
        super(message);
    }

    public ParseException(Throwable cause)
    {
        super(cause);
    }    
}
