package info.semantictext.grammar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.semantictext.GType;
import info.semantictext.Node;
import info.semantictext.parser.ParseException;
import info.semantictext.parser.Parser;
import info.semantictext.utils.IOUtils;
import info.semantictext.utils.NameUtils;

public class GrammarFactory
{
    private static Map<String, Map<String,GType>> types = new HashMap<String, Map<String,GType>>();
    
    static
    {
        try
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
            
            ns_def = generateNameSpaceMap(nameSpace);
            types.put(nameSpace, ns_def);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
    
    // ------------------
    // Generation methods
    // ------------------
    
    private static Map<String, GType> generateNameSpaceMap(String namespace) throws IOException
    {
        // Get document from local
        System.out.println("Updating namespace: " + namespace);
        
        // Get the file
        File f = GrammarRetrieve.getNameSpaceFile(namespace);    
        
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
            GrammarValidator.validate(result, namespace);
            return result;
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    private static Map<String, GType> generateMap(List<GType> listGtypes) throws ParseException
    {
        // Validate
        GrammarValidator.validate(listGtypes);
        
        // Create
        Map<String, GType> result = new HashMap<String, GType>();
        for (GType type: listGtypes)
        {
            // Make insert
            result.put(NameUtils.uniform(type.getName()), type);
            String[] alias = type.getAlias();
            for(String a: alias)
            {
                // Check alias
                result.put(NameUtils.uniform(a), type);
            }
        }
        return result;
    }
}
