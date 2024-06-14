package test;

import info.semantictext.NamespaceProcessor;
import info.semantictext.Parser;

public class AbstractTestParser
{
    protected Parser parser;
    protected NamespaceProcessor grammarProcessor;
    
    public AbstractTestParser()
    {
        parser = new Parser();
        grammarProcessor = new NamespaceProcessor();
        //grammarProcessor.debug = true;
        parser.addNodeProcessor(grammarProcessor);
    }
}
