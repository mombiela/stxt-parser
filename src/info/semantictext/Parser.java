package info.semantictext;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
    public static final String UTF8_BOM = "\uFEFF";

    // ---------
    // Constants
    // ---------

    // Patterns
    private static final Pattern COMPRESSED_LINE = Pattern.compile("^\\d+\\:.*$");
    private static final Pattern EMPTY_LINE = Pattern.compile("^\\s*$");
    private static final Pattern COMMENT_LINE = Pattern.compile("^\\s*\\#.*$");

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

    public void parseURI(String uri) throws IOException
    {
        String content = Utils.getUrlContent(new URL(uri));
        parse(content);
    }
    
    public void parse(File srcFile) throws IOException
    {
        String content = FileUtils.readFileContent(srcFile);
        parse(content);
    }
    
    public void parse(String content) throws IOException {
        // Only one execution
        if (executed)
            throw new ParseException("Parser is not thread safe. Only one execution allowed");
        else
            executed = true;

        // Get reader
        BufferedReader in = new BufferedReader(new StringReader(content));

        boolean firstLine = true;
        while ((line = in.readLine()) != null) {
            if (firstLine) {
                line = removeUTF8BOM(line);
                firstLine = false;
            }

            this.lineNum++;
            line = normalize(line, lastNode != null && lastNode.getType() != Type.NODE, lastLevel);
            if (line != null) {
                update(line);
            }
        }

        // Validate all nodes remaining in the list. Everything is finished!
        for (Node n : nodeStack)
            validateNode(n);
    }

    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    public Node getDocumentNode() {
        return nodeStack.get(0);
    }

    // ---------------
    // Update de linea
    // ---------------

    private void update(String line) throws IOException {
        // Obtain the level
        int i = line.indexOf(':');
        int maxLevel = Integer.parseInt(line.substring(0, i));
        line = line.substring(i + 1);

        // Check if it's the first node
        if (lastNode == null) {
            updateFirstNode(line, maxLevel);
        }
        // Check if it's text of the last node
        else if (isTextOfLast(maxLevel)) {
            updateTextOfLast(line, maxLevel);
            return;
        }
        // Update node
        else {
            if (line.trim().length() == 0 || line.trim().charAt(0) == '#')
                return;
            updateNode(line, maxLevel);
        }
    }

    // ------------
    // Node updates
    // ------------

    private void updateFirstNode(String line, int maxLevel) throws IOException {
        // Validate that it's level 0
        if (maxLevel != 0) {
            String error = "The first level cannot have a level";
            throw new ParseException(error);
        }

        // Obtain name and namespace
        lastNode = createNode(line, maxLevel);
        lastLevel = -1;
        nodeStack.add(lastNode);
        levelStack.add(lastLevel);
    }

    private void updateNode(String line, int level) throws IOException {
        // Obtain the node
        lastNode = createNode(line, level);

        // Validate the level
        validateLevel(level);

        // Update the stack
        updateStack(level);

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

    private void validateLevel(int level) throws ParseException {
        if (lastLevel == -1) {
            if (level > 1) {
                String error = "Line level incorrect [" + this.lineNum + "]: cannot be higher than 1";
                throw new ParseException(error);
            }
        } else {
            if (level > (lastLevel + 1)) {
                String error = "Line level incorrect [" + this.lineNum + "]: cannot be higher than " + (lastLevel + 1);
                throw new ParseException(error);
            }
        }
    }

    private void updateTextOfLast(String line, int maxLevel) {
        String value = lastNode.getValue();
        if (value.length() > 0)
            lastNode.setValue(value + '\n' + line);
        else
            lastNode.setValue(line);
    }

    private boolean isTextOfLast(int maxLevel) {
        // Verify if the last one is text
        if (lastNode.getType() == Type.NODE)
            return false;

        // Check if it's of a higher node
        return maxLevel > lastLevel;
    }

    // ----------------
    // Creation of node
    // ----------------

    private Node createNode(String line, int maxLevel) throws IOException {
        // Create result
        Node result = new Node();
        result.setLineCreation(lineNum);

        // Trim from the beginning
        int index = 0;
        while (line.charAt(index) == Constants.TAB || line.charAt(index) == Constants.SPACE) {
            index++;
        }
        line = line.substring(index);

        // Find name, namespace, and value
        int v_sep = line.indexOf(":");
        if (v_sep == -1) {
            String error = "Malformed line [" + this.lineNum + "]: " + this.line;
            throw new ParseException(error);
        }
        int i0 = line.substring(0, v_sep).indexOf('(');
        int i1 = line.substring(0, v_sep).indexOf(')');

        int v0;
        if (i1 != -1) {
            String aux = line.substring(i1);
            v0 = aux.indexOf(':') + i1;
        } else {
            v0 = line.indexOf(':');
        }

        // Check for errors
        boolean hasError = (v0 == -1) || (i0 == -1 && i1 != -1) || (i0 != -1 && i1 != -1 && i0 > i1) || (i0 != -1 && i1 == -1) || (v0 < i1);
        if (hasError) {
            String error = "Malformed line [" + this.lineNum + "]: " + this.line;
            throw new ParseException(error);
        }

        // Obtain namespace and name
        String nameSpace = null;
        String typeName = null;
        String value = line.substring(v0 + 1);
        if (value.trim().length() == 0)
            value = "";

        if (i0 == -1) {
            typeName = line.substring(0, v0);
            nameSpace = deduceNamespace(typeName, maxLevel);
        } else {
            nameSpace = line.substring(i0 + 1, i1);
            typeName = line.substring(0, i0);
        }

        // Create node
        result.setName(typeName);
        result.setNamespace(nameSpace);
        result.setValue(value);

        // Obtain the parts
        updateNode(result, maxLevel);

        return result;
    }

    private void updateNode(Node node, int level) throws IOException {
        // Validate
        NamespaceNode gtype = GrammarFactory.retrieveNamespaceType(node.getName(), node.getNamespace());
        if (gtype == null) {
            String error = "Invalid name, namespace: " + node.getName() + ", " + node.getNamespace();
            throw new ParseException(error);
        }
        node.setCanonicalName(gtype.getName());

        // Insert nodetype
        node.setType(gtype.getNodeType());
    }

    private Node getParent(int level) {
        for (int i = levelStack.size() - 1; i >= 0; i--) {
            if (levelStack.get(i) < level)
                return nodeStack.get(i);
        }
        return null;
    }

    private String deduceNamespace(String typeName, int level) throws IOException {
        // Deduce namespace
        Node parent = getParent(level);
        if (parent == null)
            throw new ParseException("Namespace deduction failed. Line [" + this.lineNum + "]");

        // Find namespace
        NamespaceNode gtype = GrammarFactory.retrieveNamespaceType(parent.getName(), parent.getNamespace());
        if (gtype == null)
            throw new ParseException("Grammar loading failed. Line [" + this.lineNum + "], " + parent.getName() + ", "
                    + parent.getNamespace());

        NamespaceNodeChild[] childs = gtype.getChilds();
        if (childs == null)
            throw new ParseException("Namespace deduction failed. Line [" + this.lineNum + "]");

        for (NamespaceNodeChild child : childs) {
            NamespaceNode typeChild = GrammarFactory.retrieveNamespaceType(child.getType(), child.getNamespace());
            if (typeChild == null) {
                System.err.println("Invalid namespace: " + child);
                throw new ParseException("Namespace deduction failed. Line [" + this.lineNum + "]");
            }
            if (typeChild.getName().equalsIgnoreCase(typeName))
                return typeChild.getNamespace();
            String[] alias = typeChild.getAlias();
            if (alias != null) {
                for (String a : alias) {
                    if (a.equalsIgnoreCase(typeName))
                        return typeChild.getNamespace();
                }
            }
        }

        // Finally, error
        throw new ParseException("Namespace deduction failed. Line [" + this.lineNum + "]");
    }

    private void updateStack(int level) throws IOException {
        int i = levelStack.size() - 1;
        while (i >= 0) {
            if (levelStack.get(i) >= level) {
                levelStack.remove(i);
                Node n = nodeStack.remove(i);
                validateNode(n); // Validate node at this point, it's complete now
            } else
                break;
            i--;
        }
    }

    private void validateNode(Node n) throws IOException {
        // Node needs validation according to its definition
        NamespaceNode gtype = GrammarFactory.retrieveNamespaceType(n.getCanonicalName(), n.getNamespace());
        NodeValidator.validate(n, gtype);
    }

    // ----------------------------
    // Compress and delete comments
    // ----------------------------

    private static String normalize(String aLine, boolean lastNodeText, int lastLevel) {
        // Validate if already compressed
        if (COMPRESSED_LINE.matcher(aLine).matches())
            return aLine;

        // Validate if empty line or comment
        if (!lastNodeText && (EMPTY_LINE.matcher(aLine).matches() || COMMENT_LINE.matcher(aLine).matches()))
            return null;

        // Obtain the level and pointer
        int level = 0;
        int spaces = 0;

        int pointer = 0;
        while (pointer < aLine.length()) {
            // Last char
            char charPointer = aLine.charAt(pointer);

            // Update level
            if (charPointer == Constants.SPACE) {
                spaces++;
                if (spaces == Constants.TAB_SPACES) {
                    level++;
                    spaces = 0;
                }
            } else if (charPointer == Constants.TAB) {
                level++;
                spaces = 0;
            } else {
                break;
            }

            // Pointer position
            pointer++;

            // Validate that text can only have one more level, so no information is lost
            if (lastNodeText && level > lastLevel)
                break;
        }

        // In case of text, check if it's a comment or not (depends on the comment's level)
        if (lastNodeText && level <= lastLevel) {
            if (EMPTY_LINE.matcher(aLine).matches())
                return (lastLevel + 1) + ":";
            if (COMMENT_LINE.matcher(aLine).matches())
                return null;
        }

        return level + ":" + aLine.substring(pointer);
    }

    // ---------
    // Test Main
    // ---------

    public static void main(String[] args) {
        System.out.println("Start");

        System.out.println(normalize("\t\t   \t    A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(normalize("4:A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(normalize("  #4:A recipe is the instructions, materials, etc.", false, 0));
        System.out.println(normalize("  #4:A recipe is the instructions, materials, etc.", true, 0));
        System.out.println(normalize("  \t   \t   ", false, 1));
        System.out.println(normalize("  \t   \t   ", true, 1));
        System.out.println(normalize("", true, 1));

        System.out.println("End");
    }
}
