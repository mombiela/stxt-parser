package info.semantictext;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// NOT THREAD SAFE!!
public class Parser 
{
    public boolean debug = false;
    
    // ----------------
    // Parseo principal
    // ----------------
    
    public void addNodeProcessor(NodeProcessor processor) 
    {
        nodeProcessors.add(processor);
    }

    public List<Node> parseURI(String uri) throws IOException, ParseException
    {
        String content = Utils.getUrlContent(new URL(uri));
        return parse(content);
    }
    
    public List<Node> parseFile(File srcFile) throws IOException, ParseException
    {
        String content = UtilsFile.readFileContent(srcFile);
        return parse(content);
    }

    // ---------------------------------
    // Variables internas funcionamiento
    // ---------------------------------
    
    private List<NodeProcessor> nodeProcessors = new ArrayList<>();
    List<Node> document = null;
    Stack<Node> stack = new Stack<>();
    Node currentRoot = null;
    int lineNumber = 0;
    int currentLevel = 0;
    
    public List<Node> parse(String content) throws ParseException, IOException 
    {
        // Inicialize all
        document = new ArrayList<Node>();
        stack = new Stack<>();
        currentRoot = null;
        lineNumber = 0;
        currentLevel = 0;
        
        // Sanitary check
        content = LineIndent.removeUTF8BOM(content);
	
        // Get reader
        BufferedReader in = new BufferedReader(new StringReader(content));

        String line = null;
        while ((line = in.readLine()) != null) 
        {
            lineNumber++;
            processLine(line);
        }

        if (currentRoot != null) 
        {
            processCompletion(currentRoot);
            document.add(currentRoot);
        }
        
        return document;
    }

    private void processLine(String line) throws ParseException, IOException
    {
        // Last node multiline
        Node lastNode = stack.size() > 0 ? stack.peek(): null;
        boolean lastNodeMultiline = lastNode != null && lastNode.isMultiline();
        
        // Parse Line
        LineIndent lineIndent = LineIndent.parseLine(line, lastNodeMultiline, stack.size(), lineNumber);
        if (lineIndent == null) return; // is a comment
        showLine(line, lineIndent);
        
        // Multiline (implícit)
        if (lastNodeMultiline && lineIndent.indentLevel>=stack.size())
        {
            addMultilineValue(lastNode, lineIndent.lineWithoutIndent);
            return;
        }
        
        // Normal parser
        if (lineIndent.indentLevel > currentLevel + 1) 
            throw new ParseException("Level of indent incorrect: " + lineIndent.indentLevel, lineNumber);
        
        currentLevel = lineIndent.indentLevel;
        Node node = createNode(lineIndent);
        
        // Multiline (explícit)
        if (node.getName()==null && lastNode != null)
        {
            addMultilineValue(lastNode, node.getValue());
            return;
        }
        
        processCreation(node);

        if (currentLevel == 0) 
        {
            if (currentRoot != null) 
            {
                processCompletion(currentRoot);
                document.add(currentRoot);
            }
            currentRoot = node;
            stack.clear();
            stack.push(currentRoot);
        } 
        else 
        {
            // Complete stack
            while (stack.size() > currentLevel) 
            {
                Node finishedNode = stack.pop();
                processCompletion(finishedNode);
            }
            
            // Normal insert child
            Node peek = stack.peek();
            processBeforeAddNode(peek, node);
            peek.addChild(node);
            processAfterAddNode(peek, node);
            stack.push(node);
        }
        
        // Final log
        showCurrentRoot();
    }

    private void addMultilineValue(Node lastNode, String value) {
	String lastValue = lastNode.getValue();
	if (lastValue != null)  lastNode.setValue(lastValue + "\n" + value);
	else                    lastNode.setValue(value);

	showCurrentRoot();
    }

    private Node createNode(LineIndent result) throws ParseException 
    {
        String line = result.lineWithoutIndent;
        String name = line;
        String value = null;
        
        int i = line.indexOf(':');
        if (i != -1)
        {
            name = line.substring(0,i).trim(); // Always trim name
            value = line.substring(i+1);
            
            if (name.isEmpty()) // Multiline
            {
        	name = null;
            }
            else
            {
        	value = value.trim();
                if (value.isEmpty()) value = null;
            }
        }
        
        Node node = new Node(lineNumber, currentLevel);
        node.setName(name == null ? name: name.toLowerCase());
        node.setValue(value);
        return node;
    }
    
    // ----------
    // Processors
    // ----------
    
    private void processCreation(Node node) throws ParseException, IOException
    {
        for (NodeProcessor processor : nodeProcessors) 
        {
            processor.processNodeOnCreation(node);
        }
    }

    private void processCompletion(Node node) throws ParseException, IOException
    {
        for (NodeProcessor processor : nodeProcessors) 
        {
            processor.processNodeOnCompletion(node);
        }
    }
    
    private void processBeforeAddNode(Node parent, Node child) throws ParseException, IOException
    {
        for (NodeProcessor processor : nodeProcessors) 
        {
            processor.processBeforeAdd(parent, child);
        }
    }
    private void processAfterAddNode(Node parent, Node child) throws ParseException, IOException
    {
        for (NodeProcessor processor : nodeProcessors) 
        {
            processor.processAfterAdd(parent, child);
        }
    }    

    // ---------------
    // Printer methods
    // ---------------
    
    private void showCurrentRoot()
    {
        if (debug) System.out.println("\n" + currentRoot);
    }

    private void showLine(String line, LineIndent result)
    {
        if (debug)
        {
            System.out.println("***********************************************************************************");
            System.out.println("Line: '" + line + "'");
            System.out.println("Line " + lineNumber + ": " + result);
        }
    }
}
