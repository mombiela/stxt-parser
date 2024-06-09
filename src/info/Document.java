package info;

import java.util.ArrayList;
import java.util.List;

public class Document 
{
    private List<Node> documents;

    public Document() {
        this.documents = new ArrayList<>();
    }

    public void addDocument(Node doc) {
        documents.add(doc);
    }

    public List<Node> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Node> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "Document{documents=" + documents + '}';
    }
}
