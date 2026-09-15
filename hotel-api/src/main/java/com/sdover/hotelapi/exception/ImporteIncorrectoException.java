package com.sdover.hotelapi.exception;

public class ImporteIncorrectoException extends RuntimeException {
    public ImporteIncorrectoException (String mensaje) {
        super(mensaje);
    }
}
