package info.semantictext.parser;

import info.semantictext.Node;
import info.semantictext.Type;
import info.semantictext.grammar.GrammarFactory;
import info.semantictext.namespace.NamespaceNode;
import info.semantictext.namespace.NamespaceNodeChild;
import info.semantictext.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Parser
{
    public static final String UTF8_BOM = "\uFEFF";
    
    // ---------
    // Constants
    // ---------
    
    // Patterns
    private static final Pattern COMPRESSED_LINE    = Pattern.compile("^\\d+\\:.*$");
    private static final Pattern EMPTY_LINE         = Pattern.compile("^\\s*$");
    private static final Pattern COMMENT_LINE       = Pattern.compile("^\\s*\\#.*$");
    
    // -------------------
    // Internal properties
    // -------------------
    
    // Actual parser line and lineNum
    private int lineNum = 0;
    private String line = null;
    private boolean executed;
    
    // Props of nodes
    private List<Node> nodeStack = new ArrayList<Node>(); 
    private Node lastNode = null;
    
    private List<Integer> levelStack = new ArrayList<Integer>(); 
    private int lastLevel;
    
    // ---------------------------
    // Principal method of parsing
    // ---------------------------
    
    public void parse(InputStream inputStream) throws IOException
    {
        // Only one execution
        if (executed)   throw new ParseException("Parser is not thread safe. Only one execution allowed");
        else            executed = true;
        
        // Get reader
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, Constants.ENCODING));

        boolean firstLine = true;
        while((line = in.readLine()) != null) 
        {
            if (firstLine) 
            {
                line = removeUTF8BOM(line);
                firstLine = false;
            }            
            
            this.lineNum++;
            line = normalize(line, lastNode != null && lastNode.getType()!=Type.NODE, lastLevel);
            if (line!=null)
            {
                update(line);
            }
        }
        
        // Validar todos los nodos que quedan en la list. Ya est� todo terminado!!
        for (Node n: nodeStack) validateNode(n);
    }
    
    private static String removeUTF8BOM(String s) 
    {
        if (s.startsWith(UTF8_BOM)) 
        {
            s = s.substring(1);
        }
        return s;
    }    
    public Node getDocumentNode()
    {
        return nodeStack.get(0);
    }
    
    // ---------------
    // Update de linea
    // ---------------
    
    private void update(String line) throws IOException
    {
        // Obtenemos el nivel
        int i = line.indexOf(':');
        int maxLevel = Integer.parseInt(line.substring(0,i));
        line = line.substring(i+1);
        
        // Comprobamos si es el primer nodo
        if (lastNode == null)
        {
            updateFirstNode(line, maxLevel);
        }
        // Comprobamos si es texto de un �ltimo nodo
        else if (isTextOfLast(maxLevel))
        {
            updateTextOfLast(line, maxLevel);
            return;
        }
        // Update node
        else 
        {
            if (line.trim().length()==0 || line.trim().charAt(0)=='#') return;
            updateNode(line, maxLevel);
        }
    }
    
    // ------------
    // Node updates
    // ------------
    
    private void updateFirstNode(String line, int maxLevel) throws IOException
    {
        // Validamos que sea de nivel 0
        if (maxLevel != 0)
        {
            String error = "Primer nivel no puede tener nivel";
            throw new ParseException(error);
        }
        
        // Obtenemos nombre y namespace
        lastNode = createNode(line, maxLevel);
        lastLevel = -1;
        nodeStack.add(lastNode);
        levelStack.add(lastLevel);
    }
    
    private void updateNode(String line, int level) throws IOException
    {
        // Obtenemos nodo
        lastNode = createNode(line, level);
        
        // Validamos nivel
        validateLevel(level);
        
        // Hacemos update de stack
        updateStack(level);
        
        // A�adimos a �ltimo nodo
        Node lastFromStack = nodeStack.get(nodeStack.size()-1);
        List<Node> childs = lastFromStack.getChilds();
        if (childs == null)
        {
            childs = new ArrayList<Node>();
            lastFromStack.setChilds(childs);
        }
        childs.add(lastNode);
        
        // A�adimos a stack
        levelStack.add(level);
        nodeStack.add(lastNode);
        lastLevel = level;
    }
    
    private void validateLevel(int level) throws ParseException
    {
        if (lastLevel == -1)
        {
            if (level > 1)
            {
                String error = "Nivel de linea incorrecto [" + this.lineNum + "]: no puede ser superior a 1" ;
                throw new ParseException(error);
            }
        }
        else
        {
            if (level > (lastLevel+1))
            {
                String error = "Nivel de linea incorrecto [" + this.lineNum + "]: no puede ser superior a " + (lastLevel+1);
                throw new ParseException(error);
            }
        }
    }

    private void updateTextOfLast(String line, int maxLevel)
    {
        String value = lastNode.getValue();
        if (value.length()>0)
            lastNode.setValue(value + '\n' + line);
        else
            lastNode.setValue(line);
    }

    private boolean isTextOfLast(int maxLevel)
    {
        // Verificamos que el �ltimo sea texto
        if (lastNode.getType() == Type.NODE) return false;
        
        // Miramos que sea de node mayor
        return maxLevel > lastLevel;
    }

    // ----------------
    // Creation of node
    // ----------------
    
    private Node createNode(String line, int maxLevel) throws IOException
    {
        // Creamos resultado
        Node result = new Node();
        result.setLineCreation(lineNum);
        
        // Hacemos trim por delante
        int index = 0;
        while (line.charAt(index)==Constants.TAB || line.charAt(index)==Constants.SPACE)
        {
            index++;
        }
        line = line.substring(index);
        
        // Buscamos nombre, namespace y value
        int v_sep = line.indexOf(":");
        if (v_sep == -1)
        {
            String error = "Linea [" + this.lineNum + "] mal formateada: " + this.line;
            throw new ParseException(error);
        }
        int i0 = line.substring(0,v_sep).indexOf('(');
        int i1 = line.substring(0,v_sep).indexOf(')');
        
        int v0;
        if (i1!=-1)
        {
            String aux = line.substring(i1);
            v0 = aux.indexOf(':') + i1;
        }
        else
        {
            v0 = line.indexOf(':');
        }           
        
        // Miramos error
        boolean hasError = (v0 == -1) || (i0 == -1 && i1!=-1) || (i0 != -1 && i1!=-1 && i0>i1) || (i0!=-1 && i1==-1) || (v0<i1); 
        if (hasError)
        {
            String error = "Linea [" + this.lineNum + "] mal formateada: " + this.line;
            throw new ParseException(error);
        }
        
        // Obtenemos namespace y nombre
        String nameSpace = null;
        String typeName = null;
        String value = line.substring(v0+1);
        if (value.trim().length()==0) value = "";
        
        if (i0 == -1)
        {
            typeName = line.substring(0,v0);
            nameSpace = deducirNameSpace(typeName, maxLevel);
        }
        else
        {
            nameSpace = line.substring(i0+1,i1);
            typeName = line.substring(0,i0);
        }
        
        // Create node
        result.setName(typeName);
        result.setNamespace(nameSpace);
        result.setValue(value);
        
        // Obtenemos las partes
        updateNode(result, maxLevel);
        
        return result;
    }

    private void updateNode(Node node, int level) throws IOException
    {
        // Validate
        NamespaceNode gtype = GrammarFactory.retrieveNamespaceType(node.getName(), node.getNamespace());
        if (gtype == null)
        {
            String error = "Invalid name, namespace: " + node.getName() + ", " + node.getNamespace();
            throw new ParseException(error);
        }
        node.setCanonicalName(gtype.getName());
        
        // Insert nodetype
        node.setType(gtype.getNodeType());
    }

    private Node getParent(int level)
    {
        for (int i = levelStack.size()-1; i>=0; i--)
        {
            if (levelStack.get(i)<level) return nodeStack.get(i);
        }
        return null;
    }

    private String deducirNameSpace(String typeName, int level) throws IOException
    {
        // Deducir namespace
        Node parent = getParent(level);
        if (parent == null) throw new ParseException("No se ha podido deducir el namespace. Linea[" + this.lineNum + "]");
        
        // Buscamos namespace
        NamespaceNode gtype = GrammarFactory.retrieveNamespaceType(parent.getName(), parent.getNamespace());
        if (gtype == null)  throw new ParseException("No se ha podido cargar la gram�tica. Linea[" + this.lineNum + "], " 
                + parent.getName() + ", " + parent.getNamespace());
        
        NamespaceNodeChild[] childs = gtype.getChilds();
        if (childs == null) throw new ParseException("No se ha podido deducir el namespace. Linea[" + this.lineNum + "]");
        
        for (NamespaceNodeChild child: childs)
        {
            NamespaceNode typeChild = GrammarFactory.retrieveNamespaceType(child.getType(), child.getNamespace());
            if (typeChild == null)
            {
                System.err.println("Namespace no v�lido: " + child);
                throw new ParseException("No se ha podido deducir el namespace. Linea[" + this.lineNum + "]");
            }
            if (typeChild.getName().equalsIgnoreCase(typeName)) return typeChild.getNamespace();
            String[] alias = typeChild.getAlias();
            if (alias != null)
            {
                for (String a: alias)
                {
                    if (a.equalsIgnoreCase(typeName)) return typeChild.getNamespace();
                }
            }
        }
        
        // Finalmente error
        throw new ParseException("No se ha podido deducir el namespace. Linea[" + this.lineNum + "]");
    }

    private void updateStack(int level) throws IOException
    {
        int i = levelStack.size()-1;
        while (i>=0)
        {
            if (levelStack.get(i)>=level)
            {
                levelStack.remove(i);
                Node n = nodeStack.remove(i);
                validateNode(n); // Validar nodo en este momento, ya est� completo
            }
            else break;
            i--;
        }
    }

    private void validateNode(Node n) throws IOException
    {
        // Hay que validar nodo seg�n su definici�n
        NamespaceNode gtype = GrammarFactory.retrieveNamespaceType(n.getCanonicalName(), n.getNamespace());
        NodeValidator.validate(n, gtype);
    }    
    
    // ----------------------------
    // Compress and delete comments
    // ----------------------------
    
    private static String normalize(String aLine, boolean lastNodeText, int lastLevel)
    {
        // Validamos si ya est� comprimida
        if (COMPRESSED_LINE.matcher(aLine).matches()) return aLine;
        
        // Validamos si es linea vac�a o comentario
        if (!lastNodeText && (EMPTY_LINE.matcher(aLine).matches()||COMMENT_LINE.matcher(aLine).matches()))
            return null;
        
        // Obtenemos el nivel, y el puntero
        int level = 0;
        int spaces = 0;
        
        int pointer = 0;
        while (pointer<aLine.length())
        {
            // Last char
            char charPointer = aLine.charAt(pointer);
            
            // Update level
            if (charPointer == Constants.SPACE)
            {
                spaces++;
                if (spaces == Constants.TAB_SPACES)
                {
                    level++;
                    spaces = 0;
                }
            }
            else if(charPointer == Constants.TAB)
            {
                level++;
                spaces = 0;
            }
            else
            {
                break;
            }
            
            // Pointer position
            pointer++;
            
            // Validamos que el texto s�lo pueda tener un nivel m�s, as� no se pierde informaci�n
            if (lastNodeText && level>lastLevel) break;
        }
        
        // En caso que se texto hay que mirar si es comentario o no (depende del nivel del comentario)
        if (lastNodeText && level<=lastLevel)
        {
            if (EMPTY_LINE.matcher(aLine).matches())    return (lastLevel+1) + ":";
            if (COMMENT_LINE.matcher(aLine).matches())  return null;
        }
        
        return level + ":" + aLine.substring(pointer);
    }
    
    // ---------
    // Test Main
    // ---------
    
    public static void main(String[] args)
    {
        System.out.println("Inicio");
        
        System.out.println(normalize("\t\t   \t    Una receta son las instrucciones, materiales, etc.",false,0));
        System.out.println(normalize("4:Una receta son las instrucciones, materiales, etc.",false,0));
        System.out.println(normalize("  #4:Una receta son las instrucciones, materiales, etc.",false,0));
        System.out.println(normalize("  #4:Una receta son las instrucciones, materiales, etc.",true,0));
        System.out.println(normalize("  \t   \t   ", false, 1));
        System.out.println(normalize("  \t   \t   ", true, 1));
        System.out.println(normalize("", true, 1));
        
        System.out.println("Fin");
    }
}
