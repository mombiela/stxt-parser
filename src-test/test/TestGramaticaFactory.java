package test;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import info.semantictext.grammar.GrammarFactory;

public class TestGramaticaFactory
{
    @Test
    public void mainTest() throws IOException
    {
        System.out.println(GrammarFactory.retrieveNamespaceType("n_def", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("ns_def", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("Child", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("namespace_definition", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("ns", "www.semantictext.info/namespace.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("ns", "www.semantictext.info/namespace.es.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("chapter", "www.stxt.info/book.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("page", "www.stxt.info/page.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("receta", "www.kocinando.com/receta.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("receta", "www.kocinando.com/receta1.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("recetario", "www.kocinando.com/recetario.old.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("recetario", "www.kocinando.com/recetario.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("recetario", "www.gymstxt.com/client.stxt"));
        System.out.println(GrammarFactory.retrieveNamespaceType("wsd", "www.semantictext.info/web_services.stxt"));
    }
}
