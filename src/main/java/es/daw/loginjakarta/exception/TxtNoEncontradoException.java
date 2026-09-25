package es.daw.loginjakarta.exception;

public class TxtNoEncontradoException extends Exception {
    public TxtNoEncontradoException(String message) {

        super("Cuidado" + message);
    }
}
