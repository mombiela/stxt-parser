package info;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser 
{
    private static final boolean debug = true;
    
    // ----------------
    // Parseo principal
    // ----------------
    
    public void addNodeProcessor(NodeProcessor processor) 
    {
        nodeProcessors.add(processor);
    }

    public Document parseURI(String uri) throws IOException, ParseException
    {
        String content = Utils.getUrlContent(new URL(uri));
        return parse(content);
    }
    
    public Document parseFile(File srcFile) throws IOException, ParseException
    {
        String content = UtilsFile.readFileContent(srcFile);
        return parse(content);
    }

    // ---------------------------------
    // Variables internas funcionamiento
    // ---------------------------------
    
    private List<NodeProcessor> nodeProcessors = new ArrayList<>();
    Document document = new Document();
    Stack<Node> stack = new Stack<>();
    Node currentRoot = null;
    int lineNumber = 0;
    int currentLevel = 0;
    
    public Document parse(String content) throws ParseException, IOException 
    {
        // Inicialize all
        document = new Document();
        stack = new Stack<>();
        currentRoot = null;
        lineNumber = 0;
        currentLevel = 0;
        
        // Sanitary check
        content = LineParser.removeUTF8BOM(content);
	
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
            document.addDocument(currentRoot);
        }
        
        return document;
    }

    private void processLine(String line) throws ParseException
    {
        // Last node
        Node lastNode = stack.size() > 0 ? stack.peek(): null;
        boolean lastNodeMultiline = lastNode != null && lastNode.isMultiline();
        
        // Parse Line
        IndentResult result = LineParser.parseLine(line, lastNodeMultiline, stack.size());
        if (result == null) return;
        showLine(line, result);
        
        // Multiline
        if (lastNodeMultiline && result.getIndentLevel()>=stack.size())
        {
            lastNode.setValue(lastNode.getValue() + "\n" + result.getLineWithoutIndent());
            showCurrentRoot();
            return;
        }
        
        // Normal parser
        if (result.getIndentLevel() > currentLevel + 1) 
            throw new ParseException("Level of indent incorrect: " + result.getIndentLevel(), lineNumber);
        
        currentLevel = result.getIndentLevel();
        Node node = createNode(result);
        processCreation(node);

        if (currentLevel == 0) 
        {
            if (currentRoot != null) 
            {
                processCompletion(currentRoot);
                document.addDocument(currentRoot);
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

    private void showCurrentRoot()
    {
        if (debug) System.out.println("\n" + currentRoot);
    }

    private void showLine(String line, IndentResult result)
    {
        if (debug)
        {
            System.out.println("***********************************************************************************");
            System.out.println("Line: '" + line + "'");
            System.out.println("Line " + lineNumber + ": " + result);
        }
    }

    private void processCreation(Node node) throws ParseException
    {
        for (NodeProcessor processor : nodeProcessors) 
        {
            processor.processNodeOnCreation(node, stack.size());
        }
    }

    private void processCompletion(Node node) throws ParseException
    {
        for (NodeProcessor processor : nodeProcessors) 
        {
            processor.processNodeOnCompletion(node);
        }
    }
    
    private void processBeforeAddNode(Node parent, Node child) throws ParseException
    {
        for (NodeProcessor processor : nodeProcessors) 
        {
            processor.processBeforeAdd(parent, child);
        }
    }
    private void processAfterAddNode(Node parent, Node child) throws ParseException
    {
        for (NodeProcessor processor : nodeProcessors) 
        {
            processor.processAfterAdd(parent, child);
        }
    }    

    private Node createNode(IndentResult result) 
    {
    	String line = result.getLineWithoutIndent();
    	String name = line;
    	String value = null;
    	
    	int i = line.indexOf(':');
    	if (i != -1)
    	{
    	    name = line.substring(0,i).trim();
    	    value = line.substring(i+1).trim();
    	}
    	
    	Node node = new Node();
    	node.setName(name);
    	node.setLineCreation(lineNumber);
    	node.setValue(value);
    	return node;
    }
}
