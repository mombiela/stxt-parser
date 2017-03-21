package info.semantictext.utils;

import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils
{
    public static final void closeQuietly(InputStream in)
    {
        try
        {
            if (in != null) in.close();
        }
        catch (Exception e)
        {
        }
    }
    public static final void closeQuietly(OutputStream in)
    {
        try
        {
            if (in != null) in.close();
        }
        catch (Exception e)
        {
        }
    }
}
