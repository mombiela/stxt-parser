package test;

import info.GrammarProcessor;
import info.Parser;

public class AbstractTestParser
{
    protected Parser parser;
    
    public AbstractTestParser()
    {
        parser = new Parser();
        parser.addNodeProcessor(new GrammarProcessor());
    }
}
