package com.sdover.hotelapi.exception;

public class ClienteNoEncontradoException extends RuntimeException {

    public ClienteNoEncontradoException (String mensaje) {
        super(mensaje);
    }
}
