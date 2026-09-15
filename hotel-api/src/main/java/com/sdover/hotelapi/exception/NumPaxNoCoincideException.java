package com.sdover.hotelapi.exception;

public class NumPaxNoCoincideException extends RuntimeException {
    public NumPaxNoCoincideException (String mensaje) {
        super(mensaje);
    }
}
