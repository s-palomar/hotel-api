package com.sdover.hotelapi.exception;

public class ReservaNoConfirmadaException extends RuntimeException {

    public ReservaNoConfirmadaException (String mensaje) {
        super(mensaje);
    }

}
