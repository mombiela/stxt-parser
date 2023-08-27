package info.semantictext.grammar;

import info.semantictext.utils.Constants;
import info.semantictext.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class GrammarRetrieve
{
    private static final String DEFS_DIR;
    
    static
    {
    	if (System.getProperty("stxt.path") != null)
    	{
    		DEFS_DIR = System.getProperty("stxt.path");
    	}
    	else
    	{
    		DEFS_DIR = "defs";
    	}
    }

    public static File getNameSpaceFile(String namespace) throws IOException
    {
        // Create local file
        File f = new File(DEFS_DIR, namespace);
        
        // Return if it's OK
        if (f.exists() && f.isFile()) return f;
        
        // Create directories
        f.getParentFile().mkdirs();
        
        // Search on the internet
        URL uri = new URL("http://" + namespace);
        String fileContent = getUrlContent(uri);
        
        // Write to file
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(f);
            out.write(fileContent.getBytes(Constants.ENCODING));
            out.flush();
        }
        finally
        {
            IOUtils.closeQuietly(out);
        }

        return f;
    }
    
    private static String getUrlContent(URL url) throws IOException
    {
        InputStream in = url.openStream();
        InputStreamReader reader = new InputStreamReader(in, Constants.ENCODING);

        StringBuilder sb = new StringBuilder(Math.max(16, in.available()));
        char[] tmp = new char[4096];
        for(int cnt; (cnt = reader.read(tmp)) > 0; ) sb.append(tmp, 0, cnt);
        
        return sb.toString();
    }    
    
    public static void main(String[] args) throws IOException
    {
        System.out.println("Start");
        
        String content = getUrlContent(new URL("http://www.stxt.info/page.stxt"));
        System.out.println(content);
        
        System.out.println("End");
    }
}
