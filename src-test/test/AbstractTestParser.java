package test;

import info.NamespaceProcessor;
import info.Parser;

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
