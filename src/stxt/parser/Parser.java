package stxt.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* Classe thread safe*/

public class Parser
{
    public boolean debug = false;

    public List<Node> parseFile(File srcFile) throws IOException, ParseException
    {
        String content = UtilsFile.readFileContent(srcFile);
        return parse(content);
    }

    public List<Node> parse(String content) throws ParseException, IOException
    {
        List<Node> document = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        Node currentRoot = null;
        int lineNumber = 0;
        int currentLevel = 0;

        content = LineIndent.removeUTF8BOM(content);
        BufferedReader in = new BufferedReader(new StringReader(content));

        String line;
        while ((line = in.readLine()) != null)
        {
            lineNumber++;
            LineProcessingResult result = processLine(line, lineNumber, currentLevel, stack, currentRoot, document);
            currentLevel = result.currentLevel;
            currentRoot = result.currentRoot;
        }

        if (currentRoot != null)
        {
            processOnCompletion(currentRoot);
            document.add(currentRoot);
        }

        return document;
    }

    private LineProcessingResult processLine(String line, int lineNumber, int currentLevel, Stack<Node> stack, Node currentRoot, List<Node> document) throws ParseException, IOException
    {

        Node lastNode = !stack.isEmpty() ? stack.peek() : null;
        boolean lastNodeMultiline = lastNode != null && lastNode.isMultiline();

        LineIndent lineIndent = LineIndent.parseLine(line, lastNodeMultiline, stack.size(), lineNumber);
        if (lineIndent == null)
            return new LineProcessingResult(currentLevel, currentRoot);

        showLine(line, lineIndent, lineNumber);

        if (lastNodeMultiline && lineIndent.indentLevel >= stack.size())
        {
            addMultilineValue(lastNode, lineIndent.lineWithoutIndent, false, lineNumber, currentLevel);
            return new LineProcessingResult(currentLevel, currentRoot);
        }

        if (lineIndent.indentLevel > currentLevel + 1)
            throw new ParseException("Level of indent incorrect: " + lineIndent.indentLevel, lineNumber);

        currentLevel = lineIndent.indentLevel;
        Node node = createNode(lineIndent, lineNumber, currentLevel);

        if (node.getName() == null && lastNode != null)
        {
            addMultilineValue(lastNode, node.getValue(), true, lineNumber, currentLevel);
            return new LineProcessingResult(currentLevel, currentRoot);
        }

        processOnCreation(node);

        if (currentLevel == 0)
        {
            if (currentRoot != null)
            {
                processOnCompletion(currentRoot);
                document.add(currentRoot);
            }
            currentRoot = node;
            stack.clear();
            stack.push(currentRoot);
        }
        else
        {
            while (stack.size() > currentLevel)
            {
                Node finishedNode = stack.pop();
                processOnCompletion(finishedNode);
            }

            Node parent = stack.peek();
            processBeforeAdd(parent, node);
            parent.addChild(node);
            stack.push(node);
        }

        showCurrentRoot(currentRoot);
        return new LineProcessingResult(currentLevel, currentRoot);
    }

    private void addMultilineValue(Node node, String value, boolean explicit, int lineNumber, int level)
    {
        node.addLine(new NodeLine(lineNumber, level, value, explicit));
        showCurrentRoot(node);
    }

    private Node createNode(LineIndent result, int lineNumber, int level) throws ParseException
    {
        String line = result.lineWithoutIndent;
        String name = line;
        String value = null;
        boolean multiline = false;

        if (line.startsWith(":"))
        {
            multiline = true;
            line = line.substring(1);
        }

        int i = line.indexOf(':');
        if (i == -1)
            throw new ParseException("Line not valid: " + line, lineNumber);

        name = line.substring(0, i).trim();
        if (name.isEmpty())
            throw new ParseException("Line not valid: " + line, lineNumber);

        value = line.substring(i + 1).trim();
        if (value.isEmpty())
            value = null;

        Node node = new Node(lineNumber, level, name.toLowerCase(), value);
        node.setMultiline(multiline);
        return node;
    }

    protected void processOnCreation(Node node) throws ParseException, IOException
    {
        // No action
    }

    protected void processOnCompletion(Node node) throws ParseException, IOException
    {
        // No action
    }

    protected void processBeforeAdd(Node parent, Node child) throws ParseException, IOException
    {
        // No action
    }

    private void showCurrentRoot(Node root)
    {
        if (debug && root != null)
            System.out.println("\n" + root);
    }

    private void showLine(String line, LineIndent result, int lineNumber)
    {
        if (debug)
        {
            System.out.println("***********************************************************************************");
            System.out.println("Line: '" + line + "'");
            System.out.println("Line " + lineNumber + ": " + result);
        }
    }
    
    private static class LineProcessingResult
    {
        int currentLevel;
        Node currentRoot;

        LineProcessingResult(int currentLevel, Node currentRoot)
        {
            this.currentLevel = currentLevel;
            this.currentRoot = currentRoot;
        }
    }    
}
