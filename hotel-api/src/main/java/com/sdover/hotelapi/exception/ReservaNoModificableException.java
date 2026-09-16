package com.sdover.hotelapi.exception;

public class ReservaNoModificableException extends RuntimeException {

    public ReservaNoModificableException (String mensaje) {
        super(mensaje);
    }
}
