package info;

import java.io.IOException;

public interface Processor
{
    boolean isNodeText(Node lastNode) throws IOException;
    String deduceNameSpace(String parentNamespace, String parentName, String childName) throws IOException;
    void validateNode(Node n) throws IOException;
    void updateNode(Node n) throws IOException;
}
