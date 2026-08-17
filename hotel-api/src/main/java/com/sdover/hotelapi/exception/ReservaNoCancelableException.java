package com.sdover.hotelapi.exception;

public class ReservaNoCancelableException extends RuntimeException {

    public ReservaNoCancelableException (String mensaje) {

        super(mensaje);
    }
}
