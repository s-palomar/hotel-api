package com.sdover.hotelapi.exception;

public class ReservaNoOcupadaException extends RuntimeException {
    public ReservaNoOcupadaException (String mensaje) {
        super(mensaje);
    }
}
