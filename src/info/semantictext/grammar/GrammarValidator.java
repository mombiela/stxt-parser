package info.semantictext.grammar;

import info.semantictext.NamespaceNode;
import info.semantictext.NamespaceNodeChild;
import info.semantictext.NodeType;
import info.semantictext.parser.ParseException;
import info.semantictext.utils.NameUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarValidator
{
    // Recorremos nodos validando
    public static void validate(Map<String, NamespaceNode> grammar, String namespace) throws IOException
    {
        Collection<NamespaceNode> types = grammar.values();
        for (NamespaceNode type: types)
        {
            validate(grammar, namespace, type);
        }
    }

    public static void validate(List<NamespaceNode> grammar) throws ParseException
    {
        Set<String> nsDef = new HashSet<String>();
        
        for (NamespaceNode type: grammar)
        {
            // Check name
            String canonicalName = NameUtils.uniform(type.getName());
            if (nsDef.contains(canonicalName))
                throw new ParseException("Duplicated name: " + canonicalName + " -> " + type.getNamespace());
            
            // Check alias
            String[] alias = type.getAlias();
            if (alias != null)
            {
                for(String a: alias)
                {
                    // Check alias
                    a = NameUtils.uniform(a);
                    if (nsDef.contains(a))
                        throw new ParseException("Duplicated alias: " + a + " -> " + type.getNamespace());
                }
            }
        }
    }
    
    private static void validate(Map<String, NamespaceNode> grammar, String namespace, NamespaceNode type) throws IOException
    {
        // Validamos que sea definici�n correcta
        if (type.getName()==null || type.getName().trim().length()==0)
        {
            throw new ParseException("Definici�n de gram�tica incorrecta: " + namespace + ", el nodo no tiene nombre definido: " + type);
        }
        if (type.getNamespace()==null || type.getNamespace().trim().length()==0)
        {
            throw new ParseException("Definici�n de gram�tica incorrecta: " + namespace + ", el nodo no tiene namespace definido: " + type);
        }
        if (type.getNodeType()==null)
        {
            throw new ParseException("Definici�n de gram�tica incorrecta: " + namespace + ", el nodo no tiene type de nodo definido: " + type);
        }
        
        // Validamos que un nodo distinto de node no tenga childs
        if (type.getNodeType()!=NodeType.NODE)
        {
            if (type.getChilds()!=null && type.getChilds().length!=0)
            {
                throw new ParseException("Definici�n de gram�tica incorrecta: " + namespace + ", el nodo no puede tener hijos: " + type);
            }
            return;
        }
        
        // Es un nodo NODE, por lo que hay que validar que tenga hijos
        NamespaceNodeChild[] childs = type.getChilds();
        if (childs == null || childs.length==0)
        {
            throw new ParseException("Definici�n de gram�tica incorrecta: " + namespace + ", el nodo debe tener hijos: " + type);
        }
        
        // Validar nombres y alias de los hijos
        Set<String> childsAlias = new HashSet<String>();
        for (NamespaceNodeChild child: childs)
        {
            // Obtenemos tipo
            NamespaceNode g = getGType(child, grammar, namespace);
            if (g == null)
            {
                throw new ParseException("Definici�n de gram�tica incorrecta: " 
                        + namespace + ", el nodo no tiene un hijo correcto: " + type + " child= " + child);
            }
            
            // Validamos nombres y alias
            // Check name
            String canonicalName = NameUtils.uniform(type.getName());
            if (childsAlias.contains(canonicalName))
                throw new ParseException("Duplicated name or childName: " + canonicalName + " -> " + type);
            
            // Check alias
            String[] alias = type.getAlias();
            if (alias != null)
            {
                for(String a: alias)
                {
                    // Check alias
                    a = NameUtils.uniform(a);
                    if (childsAlias.contains(a))
                        throw new ParseException("Duplicated name or childName: " + canonicalName + " -> " + type);
                }
            }
        }
    }

    private static NamespaceNode getGType(NamespaceNodeChild child, Map<String, NamespaceNode> grammar, String namespace) throws IOException
    {
        if (child.getNamespace().equalsIgnoreCase(namespace))
        {
            return grammar.get(NameUtils.uniform(child.getType()));
        }
        else
        {
            return GrammarFactory.retrieveGType(child.getType(), child.getNamespace());
        }
    }
}
