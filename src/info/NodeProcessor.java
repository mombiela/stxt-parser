package info;

public interface NodeProcessor {
    void processNodeOnCreation(Node node) throws ParserException;
    void processNodeOnCompletion(Node node) throws ParserException;
}
