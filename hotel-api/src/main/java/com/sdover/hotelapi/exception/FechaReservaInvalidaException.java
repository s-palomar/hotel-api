package com.sdover.hotelapi.exception;

public class FechaReservaInvalidaException extends RuntimeException {
    public FechaReservaInvalidaException (String mensaje) {
        super(mensaje);
    }
}
