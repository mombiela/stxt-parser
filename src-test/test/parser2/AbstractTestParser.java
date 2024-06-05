package test.parser2;

import java.io.File;

import info.semantictext.GrammarRetriever;
import info.semantictext.Parser2;

public class AbstractTestParser
{
    protected Parser2 parser;
    
    public AbstractTestParser()
    {
        parser = new Parser2();
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
