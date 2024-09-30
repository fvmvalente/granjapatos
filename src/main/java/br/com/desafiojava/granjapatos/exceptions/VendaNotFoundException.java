package br.com.desafiojava.granjapatos.exceptions;

public class VendaNotFoundException extends RuntimeException {
    public VendaNotFoundException(String message) {
        super(message);
    }
}
