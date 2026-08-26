package com.sdover.hotelapi.exception;

public class HabitacionYaExisteException extends RuntimeException {

    public HabitacionYaExisteException(String mensaje) {
        super(mensaje);
    }
}
