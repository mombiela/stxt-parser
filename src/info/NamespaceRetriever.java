package info;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamespaceRetriever
{
    private static Map<String, String> CACHE = new HashMap<>();
    
    public void addGrammarDefinition(String nameSpace, String content)
    {
        CACHE.put(nameSpace, content);
    }
    
    public static void addGrammarDefinitionsFromDir(File dir) throws IOException
    {
        // Read definitions from directory
        List<File> files = UtilsFile.getStxtFiles(dir);
        for (File f: files)
        {
            //System.out.println("Grammar in file: " + f);
            String name = f.getAbsolutePath().substring(dir.getAbsolutePath().length()+1);
            name = name.replaceAll("\\\\", "/");
            System.out.println("CACHE grammar: " + name);
            CACHE.put(name, UtilsFile.readFileContent(f));
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
