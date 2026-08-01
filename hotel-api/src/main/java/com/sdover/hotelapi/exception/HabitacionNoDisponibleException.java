package com.sdover.hotelapi.exception;

public class HabitacionNoDisponibleException extends RuntimeException {

    public HabitacionNoDisponibleException (String mensaje) {
        super(mensaje);
    }
}
