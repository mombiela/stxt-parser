package info;

import java.io.IOException;

public interface NodeProcessor 
{
    void processNodeOnCreation(Node node) throws ParseException, IOException;
    void processNodeOnCompletion(Node node) throws ParseException, IOException;
    void processBeforeAdd(Node parent, Node child) throws ParseException, IOException;
    void processAfterAdd(Node parent, Node child) throws ParseException, IOException;
}
