package com.sdover.hotelapi.exception;

public class HabitacionNoEncontradaException extends RuntimeException {

    public HabitacionNoEncontradaException (String mensaje) {
        super(mensaje);
    }
}
