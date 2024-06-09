package info.ia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class STXTParser {
    private List<NodeProcessor> nodeProcessors;

    public STXTParser() {
        this.nodeProcessors = new ArrayList<>();
    }

    public void addNodeProcessor(NodeProcessor processor) {
        nodeProcessors.add(processor);
    }

    public Document parse(String content) throws ParserException {
        Document document = new Document();
        Stack<Node> stack = new Stack<>();
        Node currentRoot = null;
        
        List<String> lines = Arrays.asList(content.split("\n"));

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }

            IndentResult result = getIndentLevelAndLine(line);
            int indentLevel = result.getIndentLevel();
            Node node = createNode(result, i + 1); // Pasar el número de línea al crear el nodo

            for (NodeProcessor processor : nodeProcessors) {
                processor.processNodeOnCreation(node);
            }

            if (indentLevel == 0 && node.getName().startsWith("documento")) {
                if (currentRoot != null) {
                    for (NodeProcessor processor : nodeProcessors) {
                        processor.processNodeOnCompletion(currentRoot);
                    }
                    document.addDocument(currentRoot);
                }
                currentRoot = node;
                stack.clear();
                stack.push(currentRoot);
            } else {
                while (stack.size() > indentLevel) {
                    Node finishedNode = stack.pop();
                    for (NodeProcessor processor : nodeProcessors) {
                        processor.processNodeOnCompletion(finishedNode);
                    }
                }
                if (!stack.isEmpty()) {
                    stack.peek().addChild(node);
                }
                stack.push(node);
            }
        }

        if (currentRoot != null) {
            for (NodeProcessor processor : nodeProcessors) {
                processor.processNodeOnCompletion(currentRoot);
            }
            document.addDocument(currentRoot);
        }

        return document;
    }

    private IndentResult getIndentLevelAndLine(String line) {
        int indentLevel = 0;
        while (line.startsWith("    ") || line.startsWith("\t")) {
            if (line.startsWith("    ")) {
                indentLevel++;
                line = line.substring(4);
            } else if (line.startsWith("\t")) {
                indentLevel++;
                line = line.substring(1);
            }
        }
        return new IndentResult(indentLevel, line);
    }

    private Node createNode(IndentResult result, int lineNumber) {
        String line = result.getLineWithoutIndent();
        String[] parts = line.split(":", 3);
        String name = parts[0].trim();
        String type = parts.length > 2 ? parts[1].trim() : "STRING"; // Default type to STRING if not provided
        Node node = new Node(name, type, lineNumber);
        if (parts.length > 2) {
            node.setValue(parts[2].trim());
        } else if (parts.length > 1) {
            node.setValue(parts[1].trim());
        }
        return node;
    }
}
