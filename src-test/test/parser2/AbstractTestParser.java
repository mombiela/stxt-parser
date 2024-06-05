package test.parser2;

import info.GrammarProcessor;
import info.Parser2;

public class AbstractTestParser
{
    protected Parser2 parser;
    
    public AbstractTestParser()
    {
        parser = new Parser2();
        parser.setProcessor(new GrammarProcessor());
    }
}
