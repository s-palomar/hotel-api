package com.sdover.hotelapi.exception;

public class ReservaNoEncontradaException extends RuntimeException {

    public ReservaNoEncontradaException (String mensaje) {
        super(mensaje);
    }
}
