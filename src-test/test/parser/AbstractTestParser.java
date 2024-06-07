package test.parser;

import info.StxtGrammarProcessor;
import info.Parser;

public class AbstractTestParser
{
    protected Parser parser;
    
    public AbstractTestParser()
    {
        parser = new Parser();
        parser.setProcessor(new StxtGrammarProcessor());
    }
}
