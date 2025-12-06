package dev.stxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Stack;

import dev.stxt.utils.FileUtils;

import static dev.stxt.Constants.EMPTY_NAMESPACE;

public class Parser
{
    public List<Node> parseFile(File srcFile) throws IOException, ParseException
    {
        String content = FileUtils.readFileContent(srcFile);
        return parse(content);
    }

    public List<Node> parse(String content) throws ParseException, IOException
    {
        int lineNumber = 0;

        content = removeUTF8BOM(content);
        BufferedReader in = new BufferedReader(new StringReader(content));

        ParseState state = new ParseState();
        String line;
        while ((line = in.readLine()) != null)
        {
            lineNumber++;
            processLine(line, lineNumber, state);
        }

        return state.getDocuments();
    }

    private void processLine(String line, int lineNumber, ParseState state) throws ParseException, IOException
    {
    	// Parseamos la línea actual
        LineIndent lineIndent = LineIndent.parseLine(line, lineNumber, state);
        if (lineIndent == null)
            return;
        
        int currentLevel = lineIndent.indentLevel;

        // Obtenemos valores del estado actual
    	Stack<Node> stack = state.getStack();
        Node lastNode = !stack.isEmpty() ? stack.peek() : null;
        boolean lastNodeMultiline = lastNode != null && lastNode.isMultiline();

        // Multilinea añadir si procede
        if (lastNodeMultiline && currentLevel >= stack.size())
        {
        	lastNode.addTextLine(lineIndent.lineWithoutIndent);
            return;
        }

        // Validamos nivel inválido por encima
        if (currentLevel > stack.size())
            throw new ParseException(lineNumber, "INDENTATION_LEVEL_NOT_VALID", "Level of indent incorrect: " + currentLevel);
        
        // Creamos nodo
        Node node = createNode(lineIndent, lineNumber, currentLevel, lastNode);

        if (currentLevel == 0)
        {
        	state.addDocument(node);
            stack.clear();
            stack.push(node);
        }
        else
        {
            while (stack.size() > currentLevel)
            {
                stack.pop();
            }

            Node parent = stack.peek();
            parent.getChildren().add(node);
            stack.push(node);
        }
    }

    private Node createNode(LineIndent result, int lineNumber, int level, Node parent) throws ParseException
    {
        String line = result.lineWithoutIndent;
        String name = null;
        String value = null;
        boolean multiline = false;

        int nodeIndex = line.indexOf(':');
        int textIndex = line.indexOf(">>"); 

        if ((nodeIndex != -1 && textIndex != -1) || (nodeIndex == -1 && textIndex == -1)) 
            throw new ParseException(lineNumber, "INVALID_LINE", "Line not valid: " + line);

        // Inline value
        if (nodeIndex != -1)
        {
        	name = line.substring(0, nodeIndex).trim();
        	value = line.substring(nodeIndex + 1).trim();
        }
        
        // Multiline Text
        if (textIndex != -1)
        {
        	name = line.substring(0, textIndex).trim();
        	value = line.substring(textIndex + 2).trim();
        	if (!value.isEmpty()) throw new ParseException(lineNumber, "INLINE_VALUE_NOT_VALID", "Line not valid: " + line);
        	multiline = true;
        }
        
        // Obtenemos namespace si hay
        String namespace = parent != null ? parent.getNamespace() : EMPTY_NAMESPACE;

        int namespaceIndx = name.indexOf("(");
        int namespaceEnd  = name.lastIndexOf(')');

        if (namespaceIndx != -1)
        {
            if (namespaceEnd <= namespaceIndx + 1)
                throw new ParseException(lineNumber, "INVALID_NAMESPACE_DEF", "Line not valid: " + line);

            namespace = name.substring(namespaceIndx + 1, namespaceEnd).trim();
            if (namespace.isEmpty())
                throw new ParseException(lineNumber, "INVALID_NAMESPACE_DEF", "Line not valid: " + line);

            name = name.substring(0, namespaceIndx).trim();
        }
        
        // Validamos nombre
        if (name.isEmpty())
            throw new ParseException(lineNumber, "INVALID_LINE", "Line not valid: " + line);

        // Creamos node
        Node node = new Node(lineNumber, level, name, namespace, multiline, value);
        return node;
    }
    
    private static final String UTF8_BOM = "\uFEFF";
    private static String removeUTF8BOM(String s) 
    {
        if (s.startsWith(UTF8_BOM)) s = s.substring(1);
        return s;
    }
    
}
