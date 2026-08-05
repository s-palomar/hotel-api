package com.sdover.hotelapi.exception;

public class ClienteYaExisteException extends RuntimeException {

    public ClienteYaExisteException (String mensaje) {
        super(mensaje);
    }
}
