package com.sdover.hotelapi.exception;

public class ReservaYaOcupadaException extends RuntimeException {

    public ReservaYaOcupadaException (String mensaje) {
        super(mensaje);
    }
}
