package com.sdover.hotelapi.exception;

public class ClienteDniBloqueadoException extends RuntimeException{

    public ClienteDniBloqueadoException (String mensaje) {
        
        super(mensaje);
    }
}