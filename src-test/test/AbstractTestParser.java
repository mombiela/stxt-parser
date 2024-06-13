package test;

import info.GrammarProcessor;
import info.Parser;

public class AbstractTestParser
{
    protected Parser parser;
    protected GrammarProcessor grammarProcessor;
    
    public AbstractTestParser()
    {
        parser = new Parser();
        grammarProcessor = new GrammarProcessor();
        //grammarProcessor.debug = true;
        parser.addNodeProcessor(grammarProcessor);
    }
}
