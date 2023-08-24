package test;

import java.io.IOException;

import info.semantictext.grammar.GrammarFactory;

public class TestGramaticaFactory
{
    public static void main(String[] args) throws IOException
    {
        System.out.println(GrammarFactory.retrieveGType("n_def", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("ns_def", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("Child", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("namespace_definition", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("ns", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveGType("ns", "www.semantictext.info/namespace.es.stxt"));
        System.out.println(GrammarFactory.retrieveGType("chapter", "www.stxt.info/book.stxt"));
        System.out.println(GrammarFactory.retrieveGType("page", "www.stxt.info/page.stxt"));
        System.out.println(GrammarFactory.retrieveGType("receta", "www.kocinando.com/receta.stxt"));
        System.out.println(GrammarFactory.retrieveGType("receta", "www.kocinando.com/receta1.stxt"));
        System.out.println(GrammarFactory.retrieveGType("recetario", "www.kocinando.com/recetario.old.stxt"));
        System.out.println(GrammarFactory.retrieveGType("recetario", "www.kocinando.com/recetario.stxt"));
        System.out.println(GrammarFactory.retrieveGType("recetario", "www.gymstxt.com/client.stxt"));
        System.out.println(GrammarFactory.retrieveGType("wsd", "www.semantictext.info/web_services.stxt"));
    }
}
