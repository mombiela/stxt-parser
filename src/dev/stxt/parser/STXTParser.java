package dev.stxt.parser;

import java.io.File;
import java.io.IOException;

public class STXTParser extends Parser
{
    public STXTParser(NamespaceRetriever namespaceRetriever)
    {
        addNodeProcessor(new STXTProcessor(namespaceRetriever));
    }    
    
    public STXTParser(File dirNamespaces) throws IOException, ParseException
    {
        NamespaceRetriever namespaceRetriever = new NamespaceRetriever();
        namespaceRetriever.addGrammarDefinitionsFromDir(dirNamespaces);
        addNodeProcessor(new STXTProcessor(namespaceRetriever));
    }
}
