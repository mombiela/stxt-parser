package info.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class STXTParser {
    private List<NodeValidator> nodeValidators;

    public STXTParser() {
        this.nodeValidators = new ArrayList<>();
    }

    public void addNodeValidator(NodeValidator validator) {
        nodeValidators.add(validator);
    }

    public Document parse(List<String> lines) throws ParserException {
        Document document = new Document();
        Stack<Node> stack = new Stack<>();
        Node currentRoot = null;

        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }

            IndentResult result = getIndentLevelAndLine(line);
            int indentLevel = result.getIndentLevel();
            Node node = createNode(result);

            for (NodeValidator validator : nodeValidators) {
                validator.validarNodoAlCrearse(node);
            }

            if (indentLevel == 0 && node.getName().startsWith("documento")) {
                if (currentRoot != null) {
                    for (NodeValidator validator : nodeValidators) {
                        validator.validarNodoAlFinalizar(currentRoot);
                    }
                    document.addDocument(currentRoot);
                }
                currentRoot = node;
                stack.clear();
                stack.push(currentRoot);
            } else {
                while (stack.size() > indentLevel) {
                    Node finishedNode = stack.pop();
                    for (NodeValidator validator : nodeValidators) {
                        validator.validarNodoAlFinalizar(finishedNode);
                    }
                }
                if (!stack.isEmpty()) {
                    stack.peek().addChild(node);
                }
                stack.push(node);
            }
        }

        if (currentRoot != null) {
            for (NodeValidator validator : nodeValidators) {
                validator.validarNodoAlFinalizar(currentRoot);
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

    private Node createNode(IndentResult result) {
        String line = result.getLineWithoutIndent();
        String[] parts = line.split(":", 3);
        String name = parts[0].trim();
        String type = parts.length > 2 ? parts[1].trim() : "STRING"; // Default type to STRING if not provided
        Node node = new Node(name, type);
        if (parts.length > 2) {
            node.setValue(parts[2].trim());
        } else if (parts.length > 1) {
            node.setValue(parts[1].trim());
        }
        return node;
    }
}
