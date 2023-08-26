package test;

import java.io.IOException;
import java.util.List;

import info.semantictext.NamespaceNode;
import info.semantictext.grammar.GrammarFactory;
import info.semantictext.grammar.NSGrammar;

public class TestG1
{
    public static void main(String[] args) throws IOException
    {
        List<NamespaceNode> types = NSGrammar.generateGrammar();
        for (NamespaceNode g: types) System.out.println(g);
        System.out.println(GrammarFactory.retrieveGType("ns_def", "www.semantictext.info/namespace.stxt"));
    }
}
