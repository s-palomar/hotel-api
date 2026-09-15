package com.sdover.hotelapi.exception;

public class ClienteNoCoincideException extends RuntimeException {

    public ClienteNoCoincideException (String mensaje) {
        super(mensaje);
    }
}