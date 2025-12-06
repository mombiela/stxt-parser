package test.node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dev.stxt.LineIndent;
import dev.stxt.Node;
import dev.stxt.ParseException;
import dev.stxt.ParseState;

class LineIndentTest {

    // ------------------------
    // Helpers para los tests
    // ------------------------

    private ParseState newEmptyState() {
        return new ParseState();
    }

    private ParseState newStateWithMultilineTextNode() {
        ParseState state = new ParseState();
        Node textNode = new Node(1, 0, "Content", null, true, "");
        state.getStack().push(textNode);
        return state;
    }

    // ------------------------
    // Tests fuera de multilínea
    // ------------------------

    @Test
    void emptyLine_outsideMultiline_isIgnored() throws ParseException {
        ParseState state = newEmptyState();

        LineIndent li = LineIndent.parseLine("   ", 1, state);

        assertNull(li);
    }

    @Test
    void commentLine_outsideMultiline_isNotIgnored() throws ParseException {
        ParseState state = newEmptyState();

        try
        {
        	LineIndent.parseLine("   # This is a comment", 1, state);
        	Assertions.fail("Expected ParseException for ");
        }
        catch (ParseException e)
        {
        	System.out.println("OK error: " + e.getCode());
        }
    }

    @Test
    void indentationWithSpaces_multipleOfFour_isValid() throws ParseException {
        ParseState state = newEmptyState();

        LineIndent li = LineIndent.parseLine("    Title", 1, state); // 4 espacios

        assertNotNull(li);
        assertEquals(1, li.indentLevel);
        assertEquals("Title", li.lineWithoutIndent);
    }

    @Test
    void indentationWithTab_isValid() throws ParseException {
        ParseState state = newEmptyState();

        LineIndent li = LineIndent.parseLine("\tTitle", 1, state); // 1 tab

        assertNotNull(li);
        assertEquals(1, li.indentLevel);
        assertEquals("Title", li.lineWithoutIndent);
    }

    @Test
    void indentationWithInvalidSpaces_throwsException() {
        ParseState state = newEmptyState();

        ParseException ex = assertThrows(
            ParseException.class,
            () -> LineIndent.parseLine("   Title", 1, state) // 3 espacios
        );

        assertEquals("INVALID_INDENTATION_SPACES", ex.getCode());
        assertEquals(1, ex.getLine());
    }

    @Test
    void mixedSpacesAndTabs_throwsException() {
        ParseState state = newEmptyState();

        ParseException ex = assertThrows(
            ParseException.class,
            () -> LineIndent.parseLine(" \tTitle", 1, state) // espacio + tab
        );

        assertEquals("MIXED_INDENTATION", ex.getCode());
        assertEquals(1, ex.getLine());
    }

    // ------------------------
    // Tests en multilínea (TEXT)
    // ------------------------

    @Test
    void insideMultiline_hashIsNotComment_whenIndentAtOrBeyondStackLevel() throws ParseException {
        ParseState state = newStateWithMultilineTextNode();
        // stackSize = 1, multiline = true
        // 4 espacios => level 1 == stackSize → dentro del bloque TEXT

        LineIndent li = LineIndent.parseLine("    # not a comment", 2, state);

        assertNotNull(li);
        // nivel 1, mismo que el nodo TEXT
        assertEquals(1, li.indentLevel);
        // el texto incluye el '#', no se trata como comentario
        assertEquals("# not a comment", li.lineWithoutIndent);
    }

    @Test
    void multiline_dedentEmptyLine_isPreservedAsText() throws ParseException {
        ParseState state = newStateWithMultilineTextNode();
        // stackSize = 1, multiline = true
        // línea vacía sin indentación => level 0 < stackSize → bloque especial

        LineIndent li = LineIndent.parseLine("", 3, state);

        assertNotNull(li);
        // se preserva como texto del bloque, con nivel del nodo multilínea
        assertEquals(1, li.indentLevel);
        assertEquals("", li.lineWithoutIndent);
    }

    @Test
    void multiline_dedentComment_isIgnored() throws ParseException {
        ParseState state = newStateWithMultilineTextNode();
        // stackSize = 1, multiline = true
        // comentario al nivel 0 => se ignora

        LineIndent li = LineIndent.parseLine("# outside text", 4, state);

        assertNull(li);
    }
}