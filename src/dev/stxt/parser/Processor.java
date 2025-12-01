package dev.stxt.parser;

import java.io.IOException;

public interface Processor 
{
    void processNodeOnCreation(Node node) throws ParseException, IOException;
    void processNodeOnCompletion(Node node) throws ParseException, IOException;
    void processBeforeAdd(Node parent, Node child) throws ParseException, IOException;
    void processAfterAdd(Node parent, Node child) throws ParseException, IOException;
}
