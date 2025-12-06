package dev.stxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Stack;

import info.semantictext.parser.utils.Utils;
import info.semantictext.parser.utils.UtilsFile;

public class Parser
{
    public List<Node> parseFile(File srcFile) throws IOException, ParseException
    {
        String content = UtilsFile.readFileContent(srcFile);
        return parse(content);
    }

    public List<Node> parse(String content) throws ParseException, IOException
    {
        int lineNumber = 0;

        content = Utils.removeUTF8BOM(content);
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
        Node currentRoot = state.stackPeek();

        // Multilinea añadir si procede
        if (lastNodeMultiline && currentLevel >= stack.size())
        {
        	lastNode.addTextLine(lineIndent.lineWithoutIndent);
            return;
        }

        // Validamos nivel inválido por encima
        if (currentLevel > stack.size() + 1)
            throw new ParseException(lineNumber, "IDENTATION_LEVEL_NOT_VALID", "Level of indent incorrect: " + lineIndent.indentLevel);

        // Creamos nodo
        Node node = createNode(lineIndent, lineNumber, currentLevel);

        if (currentLevel == 0)
        {
            if (currentRoot != null)
            {
                state.addDocument(currentRoot);
            }
            currentRoot = node;
            stack.clear();
            stack.push(currentRoot);
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

    private Node createNode(LineIndent result, int lineNumber, int level) throws ParseException
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
        	value = line.substring(textIndex + 1).trim();
        	if (!value.isEmpty()) throw new ParseException(lineNumber, "INLINE_VALUE_NOT_VALID", "Line not valid: " + line);
        	multiline = true;
        }
        
        if (name.isEmpty())
            throw new ParseException(lineNumber, "INVALID_LINE", "Line not valid: " + line);

        if (value.isEmpty())
            value = null;

        Node node = new Node(lineNumber, level, name, value, multiline);
        return node;
    }
}
