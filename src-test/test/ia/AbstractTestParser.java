package test.ia;

import info.ia.GrammarProcessor;
import info.ia.Parser;

public class AbstractTestParser
{
    protected Parser parser;
    
    public AbstractTestParser()
    {
        parser = new Parser();
        parser.addNodeProcessor(new GrammarProcessor());
    }
}
