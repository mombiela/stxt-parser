package info.semantictext.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.semantictext.GType;
import info.semantictext.Node;
import info.semantictext.utils.IOUtils;
import info.semantictext.utils.NameUtils;

public class GrammarFactory
{
    private static Map<String, Map<String,GType>> types = new HashMap<String, Map<String,GType>>();
    
    private static final String DEFS_DIR = "defs";
    
    static
    {
        // ------------------------------------------
        // Generera definición básica de la gramátcia
        // ------------------------------------------
        
        String nameSpace = "www.semantictext.info/namespace.stxt";
        List<GType> basicNs = NSGrammar.generateGrammar();
        Map<String, GType> ns_def = generateMap(basicNs);
        types.put(nameSpace, ns_def);
        
        // -------------------------------------------------------
        // Refrescamos con la última versión válida (de fichero!!)
        // No debería hacer falta, ya que debería ser igual,
        // pero se hace para comprobar la buena definición del
        // namespace
        // -------------------------------------------------------
        try
        {
            ns_def = generateNameSpaceMap(nameSpace);
            types.put(nameSpace, ns_def);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private static Map<String, GType> generateMap(List<GType> listGtypes)
    {
        Map<String, GType> result = new HashMap<String, GType>();
        for (GType g: listGtypes)
        {
            addType(result, g);
        }
        return result;
    }

    private static void addType(Map<String, GType> nsDef, GType type)
    {
        // Check name
        String canonicalName = NameUtils.uniform(type.getName());
        if (nsDef.containsKey(canonicalName))
            throw new IllegalArgumentException("Duplicated name: " + canonicalName + " -> " + type.getNamespace());
        
        // Check alias
        String[] alias = type.getAlias();
        if (alias != null)
        {
            for(String a: alias)
            {
                // Check alias
                a = NameUtils.uniform(a);
                if (nsDef.containsKey(a))
                    throw new IllegalArgumentException("Duplicated alias: " + a + " -> " + type.getNamespace());
            }
        }
        
        // Make insert
        nsDef.put(NameUtils.uniform(type.getName()), type);
        for(String a: alias)
        {
            // Check alias
            nsDef.put(NameUtils.uniform(a), type);
        }
    }

    
    // ----------------
    // Principal method
    // ----------------

    public static GType retrieveGType(String name, String namespace) throws IOException
    {
        name = NameUtils.uniform(name);
        
        Map<String, GType> namespaceTypes = types.get(namespace);
        if (namespaceTypes==null)
        {
            namespaceTypes = generateNameSpaceMap(namespace);
            types.put(namespace, namespaceTypes);
        }
        return namespaceTypes.get(name);
    }
    
    private static Map<String, GType> generateNameSpaceMap(String namespace) throws IOException
    {
        // Get document from local
        System.out.println("Updating namespace: " + namespace);
        
        // Get the file
        File f = new File(DEFS_DIR, namespace);
        if (!f.exists() || !f.isFile())
        {
            String error = "Namespace not found in cache dir: " + namespace;
            System.err.println(error);
            throw new ParseException(error);
        }
        
        // Loading namespace
        InputStream in = null;
        try
        {
            in = new FileInputStream(f);
            Parser p = new Parser();
            p.parse(in);
            Node node = p.getDocumentNode();
            List<GType> gtypes = NodeToGrammar.translate(node, namespace);
            Map<String, GType> result = generateMap(gtypes);
            return result;
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

}
