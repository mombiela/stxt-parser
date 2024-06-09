package test.parser_legacy;

import java.io.File;

import info.legacy.GrammarRetriever;
import info.legacy.Parser;

public class AbstractTestParser
{
    protected Parser parser;
    
    public AbstractTestParser()
    {
        parser = new Parser();
        try
        {
            GrammarRetriever.addGrammarDefinitionsFromDir(new File("defs"));
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}
