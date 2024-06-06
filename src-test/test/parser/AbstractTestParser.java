package test.parser;

import info.StxtGrammarProcessor;
import info.Parser2;

public class AbstractTestParser
{
    protected Parser2 parser;
    
    public AbstractTestParser()
    {
        parser = new Parser2();
        parser.setProcessor(new StxtGrammarProcessor());
    }
}
