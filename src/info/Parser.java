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
    private List<NodeProcessor> nodeProcessors;

    public Parser() 
    {
        this.nodeProcessors = new ArrayList<>();
    }

    public void addNodeProcessor(NodeProcessor processor) 
    {
        nodeProcessors.add(processor);
    }

    public Document parseURI(String uri) throws IOException, ParserException
    {
        String content = Utils.getUrlContent(new URL(uri));
        return parse(content);
    }
    
    public Document parseFile(File srcFile) throws IOException, ParserException
    {
        String content = UtilsFile.readFileContent(srcFile);
        return parse(content);
    }
    
    public Document parse(String content) throws ParserException, IOException 
    {
        // Sanitary check
        content = LineParser.removeUTF8BOM(content);
	
	    // Parse
        Document document = new Document();
        Stack<Node> stack = new Stack<>();
        Node currentRoot = null;
        int lineNumber = 0;
        
        // Get reader
        BufferedReader in = new BufferedReader(new StringReader(content));

        String line = null;
        while ((line = in.readLine()) != null) 
        {
            System.out.println("***********************************************************************************");
            lineNumber++;
            
            // Last node
            Node lastNode = null;
            if (stack.size()>0) lastNode = stack.peek();
            boolean lastNodeMultiline = false;
            if (lastNode != null) lastNodeMultiline = lastNode.isMultiline();
            
            // Parse Line
            IndentResult result = LineParser.parseLine(line, lastNodeMultiline, stack.size());
            System.out.println(result);

            // Commentario
            if (result == null) continue;
            
            // Multiline
            if (lastNodeMultiline && result.getIndentLevel()>stack.size())
            {
                lastNode.setValue(lastNode.getValue() + "\n" + result.getLineWithoutIndent()); // TODO Revisar caso value = null
            }
            
            // Parseo normal
            int indentLevel = result.getIndentLevel();
            Node node = createNode(result, lineNumber); // Pasar el número de línea y el namespace al crear el nodo
            
            for (NodeProcessor processor : nodeProcessors) 
            {
                processor.processNodeOnCreation(node);
            }

            if (indentLevel == 0) 
            {
                if (currentRoot != null) 
                {
                    for (NodeProcessor processor : nodeProcessors) 
                    {
                        processor.processNodeOnCompletion(currentRoot);
                    }
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
                    for (NodeProcessor processor : nodeProcessors) 
                    {
                        processor.processNodeOnCompletion(finishedNode);
                    }
                }
                if (!stack.isEmpty())
                {
                    // TODO Before add ->
                    stack.peek().addChild(node);
                }
                stack.push(node);
            }
            printAllStack("END", stack, lineNumber);
        }

        if (currentRoot != null) 
        {
            for (NodeProcessor processor : nodeProcessors) 
            {
                processor.processNodeOnCompletion(currentRoot);
            }
            document.addDocument(currentRoot);
        }
        System.out.println("***********************************************************************************");
        System.out.println("END *******************************************************************************");
        System.out.println("***********************************************************************************");
        
        return document;
    }

    private Node createNode(IndentResult result, int lineNumber) 
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

    // -------------------
    // Métodos utilitarios
    // -------------------
    
    private void printAllStack(String tag, Stack<Node> stack, int lineNum)
    {
        System.out.println(String.format("%03d", lineNum) + " " + tag + ": "
            + " LevelStack = " + stack.size()
            + ", NodeStack = "+ printNodeStack(stack) 
        );
    }

    private String printNodeStack(Stack<Node> stack)
    {
        List<String> nodeStack = new ArrayList<>();
        if (stack != null)
        {
            for (Node n: stack) nodeStack.add(n.getName());
        }
        return nodeStack.toString();
    }    
    
}
