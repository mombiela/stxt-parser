package test.parser;

import java.io.File;

import info.semantictext.GrammarRetriever;
import info.semantictext.Parser;

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
