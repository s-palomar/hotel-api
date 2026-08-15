package com.sdover.hotelapi.exception;

public class HttpMessageNotReadableException extends RuntimeException {

    public HttpMessageNotReadableException (String mensaje) {

        super(mensaje);
    }
}
