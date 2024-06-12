package info;

public interface NodeProcessor 
{
    void processNodeOnCreation(Node node) throws ParseException;
    void processNodeOnCompletion(Node node) throws ParseException;
    void processBeforeAdd(Node parent, Node child);
    void processAfterAdd(Node parent, Node child);
}
