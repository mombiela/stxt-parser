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
    
    public Document parse(String content) throws ParseException, IOException 
    {
        // Inicialize all
        document = new Document();
        stack = new Stack<>();
        currentRoot = null;
        lineNumber = 0;
        
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
        Node lastNode = null;
        if (stack.size()>0) lastNode = stack.peek();
        
        // Last node multiline
        boolean lastNodeMultiline = false;
        if (lastNode != null) lastNodeMultiline = lastNode.isMultiline();
        
        // Parse Line
        IndentResult result = LineParser.parseLine(line, lastNodeMultiline, stack.size());

        // Commentario
        if (result == null) return;
        
        // Log line // TODO Delete
        System.out.println("***********************************************************************************");
        System.out.println("Line: '" + line + "'");
        System.out.println("Line " + lineNumber + ": " + result);
        
        // Multiline
        if (lastNodeMultiline && result.getIndentLevel()>=stack.size())
        {
            lastNode.setValue(lastNode.getValue() + "\n" + result.getLineWithoutIndent()); // TODO Revisar caso value = null
            System.out.println("NEW VALUE MULTILINE: '" + lastNode.getValue() + "'");
            return;
        }
        
        // Normal parser
        int indentLevel = result.getIndentLevel();
        Node node = createNode(result);
        processCreation(node);

        if (indentLevel == 0) 
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
            while (stack.size() > indentLevel) 
            {
                Node finishedNode = stack.pop();
                processCompletion(finishedNode);
            }
            if (!stack.isEmpty())
            {
                Node peek = stack.peek();
                processBeforeAddNode(peek, node);
                peek.addChild(node);
                processAfterAddNode(peek, node);
            }
            stack.push(node);
        }
        
        // TODO Log end delete
        System.out.println("\n" + currentRoot);
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
