package info.ia;

public class ExampleNodeProcessor implements NodeProcessor {
    @Override
    public void processNodeOnCreation(Node node) throws ParserException {
        // Ejemplo de procesamiento al crear: nombre del nodo no puede ser vacío
        if (node.getName().isEmpty()) {
            throw new ParserException("El nombre del nodo no puede estar vacío");
        }
        // Ejemplo de procesamiento al crear: el tipo del nodo no puede ser vacío
        if (node.getType().isEmpty()) {
            throw new ParserException("El tipo del nodo no puede estar vacío");
        }
    }

    @Override
    public void processNodeOnCompletion(Node node) throws ParserException {
        // Ejemplo de procesamiento al finalizar: el valor del nodo debe estar presente si el tipo es STRING
        if ("STRING".equals(node.getType()) && (node.getValue() == null || node.getValue().isEmpty())) {
            throw new ParserException("El valor del nodo no puede estar vacío para los nodos de tipo STRING");
        }
        // Puedes agregar más procesamientos aquí
    }
}
