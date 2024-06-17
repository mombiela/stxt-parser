package info.semantictext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NamespaceRetriever
{
    private Map<String, Namespace> CACHE = new HashMap<>();
    private boolean allowInternet = false;
    
    public NamespaceRetriever()
    {
    }
    public NamespaceRetriever(boolean allowInternet)
    {
        this.allowInternet = allowInternet;
    }
    
    public void addGrammarDefinition(String content) throws IOException, ParseException
    {
        addGrammarDefinition(content, null);
    }
    
    public Set<String> getAllNamespaces()
    {
        return new HashSet<>(CACHE.keySet());
    }
    
    private void addGrammarDefinition(String content, String expected) throws IOException, ParseException
    {
        // Parse raw Content
        Parser parser = new Parser();
        List<Node> namespacesNodes = parser.parse(content);
        
        // Get result
        List<Namespace> namespaces = new ArrayList<>();
        {
            for (Node n: namespacesNodes) 
                namespaces.add(NamespaceTransformer.transformRawNode(n));
        }
        
        if (expected != null && (namespaces.size()!= 1 || !namespaces.get(0).getName().equals(expected)))
            throw new ParseException("Namespace is " + namespaces.get(0).getName() + ", expected: " + expected, 0);
        
        // Add result
        for (Namespace ns: namespaces)
        {
            String name = ns.getName();
            if (CACHE.containsKey(name)) throw new ParseException("Namespace already exist: " + name, 0);
            CACHE.put(name, ns);
        }
    }      
    
    public void addGrammarDefinitionsFromDir(File dir) throws IOException, ParseException
    {
        // Read definitions from directory
        List<File> files = UtilsFile.getStxtFiles(dir);
        for (File f: files)
        {
            try
            {
                addGrammarDefinition(UtilsFile.readFileContent(f));
            }
            catch (Exception e)
            {
                System.err.println("Error processing: " + f.getAbsolutePath() + " -> " + e.getMessage());
                throw e;
            }
        }
    }
    
    
    public void addGrammarDefinitionsFromFile(File file) throws IOException, ParseException
    {
        // Read definitions from directory
        addGrammarDefinition(UtilsFile.readFileContent(file));
    }
    
    public Namespace getNameSpace(String namespace) throws IOException, ParseException
    {
        if (CACHE.containsKey(namespace)) return CACHE.get(namespace);
	
        // Search on the internet
        if (allowInternet)
        {
            URL uri = new URL("https://" + namespace);
            String fileContent = Utils.getUrlContent(uri);
            addGrammarDefinition(fileContent, namespace);
        }

        return CACHE.get(namespace);
    }
}
