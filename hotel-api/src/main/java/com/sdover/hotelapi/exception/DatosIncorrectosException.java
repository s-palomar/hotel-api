package com.sdover.hotelapi.exception;

public class DatosIncorrectosException extends RuntimeException {
    public DatosIncorrectosException (String mensaje) {
        super(mensaje);
    }
}
