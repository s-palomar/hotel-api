package com.sdover.hotelapi.exception;

public class HotelNoEncontradoException extends RuntimeException {

    public HotelNoEncontradoException(String mensaje) {
        super(mensaje);
    }

}
