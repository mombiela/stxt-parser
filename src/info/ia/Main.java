package info.ia;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileContent = """
        # Doc 1
        documento(namespace):
            titulo: El título del documento 1
            autor: El nombre autor 1
            cuerpo:TEXT:
                Esta es una línea del cuerpo del documento 1
                Esta es otra línea del cuerpo del documento 1
            fecha creacion:DATE: 2024-06-09

        # Doc 2
        documento(namespace):
            titulo: El título del documento 2
            autor: El nombre autor 2
            cuerpo:TEXT:
                Esta es una línea del cuerpo del documento 2
                Esta es otra línea del cuerpo del documento 2
            fecha creacion:DATE: 2024-06-10
        """;

        List<String> lines = Arrays.asList(fileContent.split("\n"));
        STXTParser parser = new STXTParser();
        parser.addNodeValidator(new ExampleNodeValidator()); // Añadir el validador de nodos
        try {
            Document document = parser.parse(lines);
            System.out.println(document);
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }
}
