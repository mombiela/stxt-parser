package info;

import java.io.IOException;

import info.semantictext.Node;

public interface Processor
{
    boolean isNodeText(Node lastNode) throws IOException;
    String deduceNameSpace(Node parent, String typeName, int level) throws IOException;
    void validateNode(Node n) throws IOException;
}
