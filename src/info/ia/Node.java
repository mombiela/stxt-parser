package info.ia;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name;
    private String value;
    private String type;
    private List<Node> children;

    public Node(String name, String type) {
        this.name = name;
        this.type = type;
        this.children = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return "Node{name='" + name + "', type='" + type + "', value='" + value + "', children=" + children + '}';
    }
}
