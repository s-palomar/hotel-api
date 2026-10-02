package com.sdover.hotelapi.exception;

public class HabitacionConReservasActivasException extends RuntimeException {

    public HabitacionConReservasActivasException (String mensaje) {
        super(mensaje);
    }

}
