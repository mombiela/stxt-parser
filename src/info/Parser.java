package info;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    public Document parse(String content) throws ParserException 
    {
	// Sanitary check
	content = LineNormalizer.removeUTF8BOM(content);
	
	// Parse
        Document document = new Document();
        Stack<Node> stack = new Stack<>();
        Node currentRoot = null;
        
        List<String> lines = Arrays.asList(content.split("\n"));

        int lineNumber = 0;
        for (String line: lines) 
        {
            System.out.println("***********************************************************************************");
            lineNumber++;
            
            // Last node
            Node lastNode = null;
            if (stack.size()>0) lastNode = stack.peek();
            
            // Parse Line
            IndentResult result = LineNormalizer.parseLine(line, lastNode != null && lastNode.isMultiline(), stack.size());
            System.out.println(result);
            if (result == null) continue;
            
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
	String[] parts = line.split(":", 3);
	String name = parts[0].trim();
	
	Node node = new Node();
	node.setName(name);
	node.setLineCreation(lineNumber);
	if (parts.length > 2) {
	    node.setValue(parts[2].trim());
	} else if (parts.length > 1) {
	    node.setValue(parts[1].trim());
	}
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
