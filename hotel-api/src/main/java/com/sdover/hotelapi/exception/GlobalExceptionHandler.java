package com.sdover.hotelapi.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

        @ExceptionHandler(HabitacionNoEncontradaException.class)
        public ResponseEntity<ErrorResponse> manejarHabitacionNoEncontrada(
                        HabitacionNoEncontradaException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.NOT_FOUND.value());

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(error);
                }

        @ExceptionHandler(ReservaNoEncontradaException.class)
        public ResponseEntity<ErrorResponse> manejarReservaNoEncontrada(
                        ReservaNoEncontradaException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.NOT_FOUND.value());

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

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> manejarErroresValidacion(MethodArgumentNotValidException ex) {

                Map<String, String> tipoError = new HashMap<>();
                List<FieldError> errores = ex.getBindingResult().getFieldErrors();

                for (FieldError error : errores) {

                String campo = error.getField();
                String mensaje = error.getDefaultMessage(); 

                tipoError.put(campo, mensaje);             
                }

                ErrorResponse error = new ErrorResponse(
                        "Los datos enviados no son válidos",
                        HttpStatus.BAD_REQUEST.value(),
                        tipoError
                );

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(error);
        }

        @ExceptionHandler(HabitacionNoDisponibleException.class)
        public ResponseEntity<ErrorResponse> manejarHabitacionNoDisponible (
                        HabitacionNoDisponibleException  e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }

        @ExceptionHandler(FechasReservaInvalidasException.class)
        public ResponseEntity<ErrorResponse> manejarFechasReservaInvalidas(
                        FechasReservaInvalidasException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value());

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(error);
        }

        @ExceptionHandler(ClienteYaExisteException.class)
                public ResponseEntity<ErrorResponse> manejarClienteYaExiste(
                        ClienteYaExisteException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
                }

        @ExceptionHandler(ClienteNoEncontradoException.class)
        public ResponseEntity<ErrorResponse> manejarClienteNoEncontrado(
                ClienteNoEncontradoException e) {

        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
        }

        @ExceptionHandler(ClienteTieneReservasException.class)
                public ResponseEntity<ErrorResponse> manejarClienteTieneReservasException(
                        ClienteTieneReservasException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
                }        
}
