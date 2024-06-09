package test.parser_old;

import info.old.Parser;
import info.old.StxtGrammarProcessor;

public class AbstractTestParser
{
    protected Parser parser;
    
    public AbstractTestParser()
    {
        parser = new Parser();
        parser.setProcessor(new StxtGrammarProcessor());
    }
}
