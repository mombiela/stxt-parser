package test.parser;

import info.GrammarProcessor;
import info.Parser3;

public class AbstractTestParser
{
    protected Parser3 parser;
    
    public AbstractTestParser()
    {
        parser = new Parser3();
        parser.setProcessor(new GrammarProcessor());
    }
}
