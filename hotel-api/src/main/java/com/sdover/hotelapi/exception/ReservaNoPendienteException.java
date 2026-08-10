package com.sdover.hotelapi.exception;

public class ReservaNoPendienteException extends RuntimeException {

    public ReservaNoPendienteException (String mensaje) {
        super(mensaje);
    }
}
