package info.semantictext.grammar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.semantictext.NamespaceNode;
import info.semantictext.Node;
import info.semantictext.parser.ParseException;
import info.semantictext.parser.Parser;
import info.semantictext.utils.IOUtils;
import info.semantictext.utils.NameUtils;

public class GrammarFactory
{
    private static Map<String, Map<String,NamespaceNode>> types = new HashMap<String, Map<String,NamespaceNode>>();
    
    static
    {
        try
        {
            // ------------------------------------------
            // Generera definici�n b�sica de la gram�tcia
            // ------------------------------------------
            
            String nameSpace = "www.semantictext.info/namespace.stxt";
            List<NamespaceNode> basicNs = NSGrammar.generateGrammar();
            Map<String, NamespaceNode> ns_def = generateMap(basicNs);
            types.put(nameSpace, ns_def);
            
            // -------------------------------------------------------
            // Refrescamos con la �ltima versi�n v�lida (de fichero!!)
            // No deber�a hacer falta, ya que deber�a ser igual,
            // pero se hace para comprobar la buena definici�n del
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

    public static NamespaceNode retrieveGType(String name, String namespace) throws IOException
    {
        name = NameUtils.uniform(name);
        
        Map<String, NamespaceNode> namespaceTypes = types.get(namespace);
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
    
    private static Map<String, NamespaceNode> generateNameSpaceMap(String namespace) throws IOException
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
            List<NamespaceNode> gtypes = NodeToGrammar.translate(node, namespace);
            Map<String, NamespaceNode> result = generateMap(gtypes);
            GrammarValidator.validate(result, namespace);
            return result;
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }

    private static Map<String, NamespaceNode> generateMap(List<NamespaceNode> listGtypes) throws ParseException
    {
        // Validate
        GrammarValidator.validate(listGtypes);
        
        // Create
        Map<String, NamespaceNode> result = new HashMap<String, NamespaceNode>();
        for (NamespaceNode type: listGtypes)
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
