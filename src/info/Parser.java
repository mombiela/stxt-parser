package info;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parser 
{
    // -------------------
    // Internal properties
    // -------------------

    private boolean executed;
    
    // Actual parser line and lineNum
    private int lineNum;
    private String line = null;
    private int level;
    private int lastLevel;
    private boolean lastNodeText = false;

    // Props of nodes
    private List<Node> nodeStack = new ArrayList<Node>();
    private Node lastNode = null;
    private List<Integer> levelStack = new ArrayList<Integer>();
    
    // Procesadores
    private Processor mainProcessor;
    
    public void setProcessor(Processor processor)
    {
        this.mainProcessor = processor;
    }

    // ---------------------------
    // Principal method of parsing
    // ---------------------------

    public void parseURI(String uri) throws IOException
    {
        String content = Utils.getUrlContent(new URL(uri));
        parse(content);
    }
    
    public void parseFile(File srcFile) throws IOException
    {
        String content = UtilsFile.readFileContent(srcFile);
        parse(content);
    }
    
    public void parse(String content) throws IOException 
    {
        // Only one execution
        if (executed)
            throw new ParseException("Parser is not thread safe. Only one execution allowed");
        else
            executed = true;

        // Get reader
        BufferedReader in = new BufferedReader(new StringReader(content));

        while ((line = in.readLine()) != null) 
        {
            this.lineNum++;
            
            if (lineNum == 1) 
            {
                line = LineNormalizer.removeUTF8BOM(line);
            }

            System.out.println("**************************************************************");
            System.out.println("Original Line:   " + line);
            line = LineNormalizer.normalize(line, lastNodeText, lastLevel);
            System.out.println("After normalize: " + line);
            if (line != null) 
            {
                int i = line.indexOf(':');
                level = Integer.parseInt(line.substring(0, i));
                line = line.substring(i + 1);
                
                printAllStack("INI", true);
                update();
                printAllStack("FIN", false);
            }
        }

        // Validate all nodes remaining in the list. Everything is finished!
        for (Node n : nodeStack)
        {
            validateNodeAndUpdate(n);
        }
    }

    public Node getDocumentNode() 
    {
        return nodeStack.get(0);
    }

    // ---------------
    // Update de linea
    // ---------------

    private void update() throws IOException 
    {
        // Obtain the level
        if (lastNode == null)         // Check if it's the first node
        {
            updateFirstNode();
        }
        else if (isTextOfLast()) // Check if it's text of the last node 
        {
            updateTextOfLast();
        }        
        else // Update node 
        {
            if (line.trim().length() == 0 || line.trim().charAt(0) == '#') return;
            updateNode();
        }
        if (lastNode != null)
        {
            lastNodeText = mainProcessor.isNodeText(lastNode);
        }
    }

    // ------------
    // Node updates
    // ------------

    private void updateFirstNode() throws IOException 
    {
        // Obtain name and namespace
        lastNode = createNode();
        lastLevel = -1; // This is necessary to allow first level without identation
        nodeStack.add(lastNode);
        levelStack.add(lastLevel);
    }

    private void updateNode() throws IOException 
    {
        // Obtain the node
        lastNode = createNode();

        // Validate the level
        validateLevel();

        // Update the stack
        updateStack();

        // Add to the last node
        Node lastFromStack = nodeStack.get(nodeStack.size() - 1);
        List<Node> childs = lastFromStack.getChilds();
        if (childs == null) {
            childs = new ArrayList<Node>();
            lastFromStack.setChilds(childs);
        }
        childs.add(lastNode);

        // Add to the stack
        levelStack.add(level);
        nodeStack.add(lastNode);
        lastLevel = level;
    }

    private void validateLevel() throws ParseException 
    {
        if (lastLevel == -1) 
        {
            if (level > 1) 
            {
                String error = "Line level incorrect [" + this.lineNum + "]: cannot be higher than 1";
                throw new ParseException(error);
            }
        } 
        else 
        {
            if (level > (lastLevel + 1)) 
            {
                String error = "Line level incorrect [" + this.lineNum + "]: cannot be higher than " + (lastLevel + 1);
                throw new ParseException(error);
            }
        }
    }

    private void updateTextOfLast() 
    {
        String value = lastNode.getValue();
        if (value.length() > 0)   lastNode.setValue(value + '\n' + line);
        else                      lastNode.setValue(line);
    }

    private boolean isTextOfLast() throws IOException 
    {
        // Verify if the last one is text
        if (!mainProcessor.isNodeText(lastNode))
            return false;

        // Check if it's of a higher node
        return level > lastLevel;
    }

    // ----------------
    // Creation of node
    // ----------------

    private Node createNode() throws IOException 
    {
        String line = this.line;
        
        // Create result
        Node result = new Node();
        result.setLineCreation(lineNum);

        // Trim from the beginning
        int index = 0;
        while (line.charAt(index) == Constants.TAB || line.charAt(index) == Constants.SPACE) 
        {
            index++;
        }
        line = line.substring(index);

        // Find name, namespace, and value
        int v_sep = line.indexOf(":");
        if (v_sep == -1) 
        {
            String error = "Malformed line [" + this.lineNum + "]: " + this.line;
            throw new ParseException(error);
        }
        int i0 = line.substring(0, v_sep).indexOf('(');
        int i1 = line.substring(0, v_sep).indexOf(')');

        int v0;
        if (i1 != -1) 
        {
            String aux = line.substring(i1);
            v0 = aux.indexOf(':') + i1;
        }
        else 
        {
            v0 = line.indexOf(':');
        }

        // Check for errors
        boolean hasError = (v0 == -1) 
                || (i0 == -1 && i1 != -1) 
                || (i0 != -1 && i1 != -1 && i0 > i1) 
                || (i0 != -1 && i1 == -1) 
                || (v0 < i1);
        
        if (hasError) 
        {
            String error = "Malformed line [" + this.lineNum + "]: " + this.line;
            throw new ParseException(error);
        }

        // Obtain namespace and name
        String nameSpace = null;
        String typeName = null;
        String value = line.substring(v0 + 1);
        if (value.trim().length() == 0) value = "";

        if (i0 == -1) 
        {
            typeName = line.substring(0, v0);
            nameSpace = deduceNamespace(typeName, level);
        }
        else 
        {
            nameSpace = line.substring(i0 + 1, i1);
            typeName = line.substring(0, i0);
        }

        // Create node
        result.setName(typeName);
        result.setNamespace(nameSpace);
        result.setValue(value);

        return result;
    }

    private Node getParent(int level) 
    {
        for (int i = levelStack.size() - 1; i >= 0; i--) 
        {
            if (levelStack.get(i) < level)
                return nodeStack.get(i);
        }
        return null;
    }

    private String deduceNamespace(String typeName, int level) throws IOException 
    {
        // Deduce namespace
        Node parent = getParent(level);
        if (parent == null)
            throw new ParseException("Namespace deduction failed. Line [" + this.lineNum + "]");
        
        String nameSpace = mainProcessor.deduceNameSpace(parent.getNamespace(), parent.getName(), typeName);
        if (nameSpace != null)  return nameSpace;
        else                    throw new ParseException("Namespace deduction failed. Line [" + this.lineNum + "]");
    }

    private void updateStack() throws IOException {
        int i = levelStack.size() - 1;
        while (i >= 0) {
            if (levelStack.get(i) >= level) {
                levelStack.remove(i);
                Node n = nodeStack.remove(i);
                validateNodeAndUpdate(n); // Validate node at this point, it's complete now
            } else
                break;
            i--;
        }
    }

    private void validateNodeAndUpdate(Node n) throws IOException 
    {
        mainProcessor.validateNode(n);
        mainProcessor.updateNode(n);
    }

    private void printAllStack(String tag, boolean printLine)
    {
        System.out.println("Línea " + String.format("%03d", lineNum) + " " + tag + ": "
            + (printLine ? line + "\n": "\n")  
            + " LevelStack = " + levelStack 
            + ", NodeStack = "+ printNodeStack() 
            + " lastLevel = " + lastLevel 
            + " level = " + level 
        );
    }

    private String printNodeStack()
    {
        List<String> nodeStack = new ArrayList();
        if (this.nodeStack != null)
        {
            for (Node n: this.nodeStack) nodeStack.add(n.getName());
        }
        return nodeStack.toString();
    }    
}
