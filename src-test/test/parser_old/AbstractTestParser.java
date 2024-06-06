package test.parser_old;

import java.io.File;

import info.old.GrammarRetriever;
import info.old.Parser;

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
