package info.ia;

public class ExampleNodeValidator implements NodeValidator {
    @Override
    public void validarNodoAlCrearse(Node node) throws ParserException {
        // Ejemplo de validación al crear: nombre del nodo no puede ser vacío
        if (node.getName().isEmpty()) {
            throw new ParserException("El nombre del nodo no puede estar vacío");
        }
        // Ejemplo de validación al crear: el tipo del nodo no puede ser vacío
        if (node.getType().isEmpty()) {
            throw new ParserException("El tipo del nodo no puede estar vacío");
        }
    }

    @Override
    public void validarNodoAlFinalizar(Node node) throws ParserException {
        // Ejemplo de validación al finalizar: el valor del nodo debe estar presente si el tipo es STRING
        if ("STRING".equals(node.getType()) && (node.getValue() == null || node.getValue().isEmpty())) {
            throw new ParserException("El valor del nodo no puede estar vacío para los nodos de tipo STRING");
        }
        // Puedes agregar más validaciones aquí
    }
}
