package info;

import java.io.IOException;

public class ParseException extends IOException 
{
    private static final long serialVersionUID = 1L;

    public ParseException(String message)
    {
        super(message);
    }
}
