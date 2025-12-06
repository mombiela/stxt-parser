package test.node;

import dev.stxt.Node;
import dev.stxt.json.JSONArray;
import dev.stxt.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest 
{
    @Test
    void toJson_minimalNode_hasEmptyArraysAndNoOptionalFields() 
    {
        Node node = new Node(1, 0, "Title", null, false, null);

        JSONObject json = node.toJson();
        System.out.println("JSON: " + json.toString(3));

        // Campos básicos
        assertEquals("Title", json.getString("name"));
        assertEquals(1, json.getInt("line"));
        assertEquals(0, json.getInt("level"));

        // Opcionales no presentes
        assertFalse(json.has("namespace"));
        assertFalse(json.has("type"));
        assertFalse(json.has("value"));

        // Arrays siempre presentes y vacíos
        JSONArray text = json.getJSONArray("text");
        assertNotNull(text);
        assertEquals(0, text.length());

        JSONArray children = json.getJSONArray("children");
        assertNotNull(children);
        assertEquals(0, children.length());
    }

    @Test
    void toJson_fullNodeWithTextAndChild_isCorrect() 
    {
        Node parent = new Node(1, 0, "Document", null, true, "My doc");
        parent.setNamespace("dev.stxt.example");
        parent.getText().add("Line 1");
        parent.getText().add("Line 2");

        Node child = new Node(2, 1, "Title", null, false, "Hello");
        parent.getChildren().add(child);

        JSONObject json = parent.toJson();
        System.out.println("JSON: " + json.toString(3));

        // Campos básicos
        assertEquals("Document", json.getString("name"));
        assertEquals("dev.stxt.example", json.getString("namespace"));
        assertEquals("My doc", json.getString("value"));
        assertEquals(1, json.getInt("line"));
        assertEquals(0, json.getInt("level"));

        // Text
        JSONArray text = json.getJSONArray("text");
        assertEquals(2, text.length());
        assertEquals("Line 1", text.getString(0));
        assertEquals("Line 2", text.getString(1));

        // Children
        JSONArray children = json.getJSONArray("children");
        assertEquals(1, children.length());

        JSONObject childJson = children.getJSONObject(0);
        assertEquals("Title", childJson.getString("name"));
        assertEquals("Hello", childJson.getString("value"));
        assertEquals(2, childJson.getInt("line"));
        assertEquals(1, childJson.getInt("level"));

        // El hijo también debe tener siempre arrays text / children
        assertNotNull(childJson.getJSONArray("text"));
        assertNotNull(childJson.getJSONArray("children"));
        assertEquals(0, childJson.getJSONArray("text").length());
        assertEquals(0, childJson.getJSONArray("children").length());
    }
}