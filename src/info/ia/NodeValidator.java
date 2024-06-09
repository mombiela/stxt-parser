package info.ia;

public interface NodeValidator {
    void validarNodoAlCrearse(Node node) throws ParserException;
    void validarNodoAlFinalizar(Node node) throws ParserException;
}
