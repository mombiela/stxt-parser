package test;

import java.io.IOException;

import info.semantictext.grammar.GrammarFactory;

public class TestGramaticaFactory
{
    public static void main(String[] args) throws IOException
    {
        System.out.println(GrammarFactory.retrieveGType("type_def", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("Type Definition", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("Child", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("namespace_definition", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("ns", "www.semantictext.info/namespace.stxt"));
    }
}
