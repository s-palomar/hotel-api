package com.sdover.hotelapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HotelYaExisteException.class)
    public ResponseEntity<String> manejarHotelYaExiste(HotelYaExisteException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT) // devuelve un 409
                .body(e.getMessage()); // envía el mensaje de la excepción al cliente
    }

}
