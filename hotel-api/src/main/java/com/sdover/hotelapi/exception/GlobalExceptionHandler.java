package com.sdover.hotelapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sdover.hotelapi.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HotelNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarHotelNoEncontrado(HotelNoEncontradoException e) {

        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(HotelYaExisteException.class)
    public ResponseEntity<ErrorResponse> manejarHotelYaExiste(HotelYaExisteException e) {

        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.CONFLICT.value()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

}
