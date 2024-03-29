package test;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import info.semantictext.grammar.GrammarFactory;
import info.semantictext.grammar.RootGrammar;
import info.semantictext.namespace.NamespaceNode;

public class TestG1
{
    @Test
    public void mainTest() throws IOException
    {
        List<NamespaceNode> types = RootGrammar.generateRootGrammar();
        for (NamespaceNode g: types) System.out.println(g);
        System.out.println(GrammarFactory.retrieveNamespaceType("ns_def", "www.semantictext.info/namespace.stxt"));
    }
}
