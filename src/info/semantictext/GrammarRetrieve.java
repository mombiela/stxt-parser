package info.semantictext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammarRetrieve
{
    private static final String DEFS_DIR;
    private static Map<String, String> CACHE = new HashMap<>();
    
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
    	try
    	{
    	    // Read definitions from classpath
    	    System.out.println("INSERTING ROOT NAMESPACE in CACHE");
    	    CACHE.put(Constants.ROOT_NAMESPACE, RootGrammar.getRootGrammarContentString());
    	    
    	    // Read definitions from directory
    	    File dir = new File(DEFS_DIR);
    	    if (dir.isDirectory() && dir.exists())
    	    {
    		List<File> files = FileUtils.getStxtFiles(dir.getAbsolutePath());
        	for (File f: files)
        	{
        	    //System.out.println("Grammar in file: " + f);
        	    String name = f.getAbsolutePath().substring(dir.getAbsolutePath().length()+1);
        	    name = name.replaceAll("\\\\", "/");
        	    System.out.println("CACHE grammar: " + name);
        	    CACHE.put(name, FileUtils.readFileContent(f));
        	}
    	    }
    	}
    	catch (Exception e)
    	{
    	    e.printStackTrace();
    	}
    }

    public static String getNameSpaceContent(String namespace) throws IOException
    {
	if (CACHE.containsKey(namespace)) return CACHE.get(namespace);
	
        // Search on the internet
        URL uri = new URL("https://" + namespace);
        String fileContent = Utils.getUrlContent(uri);
        
        CACHE.put(namespace, fileContent);

        return CACHE.get(namespace);
    }
}
