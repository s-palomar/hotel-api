package com.sdover.hotelapi.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

        @ExceptionHandler(CheckinFueraDeFechaException.class)
        public ResponseEntity<ErrorResponse> manejarCheckinFueraDeFechaException(
                        CheckinFueraDeFechaException e) {

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
        public ResponseEntity<ErrorResponse> manejarClienteNoEncontradoException(
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

        @ExceptionHandler(ClienteNoCoincideException.class)
        public ResponseEntity<ErrorResponse> manejarClienteNoCoincideException(
                ClienteNoCoincideException e) {

        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.CONFLICT.value());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
        }

        @ExceptionHandler(AcompananteNoEncontradoException.class)
        public ResponseEntity<ErrorResponse> manejarAcompananteNoEncontradoException(
                AcompananteNoEncontradoException e) {

        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
        }
                
        @ExceptionHandler(ReservaNoPendienteException.class)
        public ResponseEntity<ErrorResponse> manejarReservaNoPendienteException(
                ReservaNoPendienteException e) {

        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.CONFLICT.value());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
        }    
                
        @ExceptionHandler(ClienteDniBloqueadoException.class)
        public ResponseEntity<ErrorResponse> manejarClienteDniBloqueadoException(
                ClienteDniBloqueadoException e) {

        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.CONFLICT.value());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
        }    

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> manejarHttpMessageNotReadableException(
                HttpMessageNotReadableException e) {

                ErrorResponse error = new ErrorResponse(
                        "El formato de los datos enviados no es válido.",
                        HttpStatus.BAD_REQUEST.value());

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(error);
        }  

        @ExceptionHandler(ReservaNoCancelableException.class)
        public ResponseEntity<ErrorResponse> manejarReservaNoCancelableException(
                ReservaNoCancelableException e) {

        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.CONFLICT.value());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
        }

        @ExceptionHandler(ReservaUpdateVaciaException.class)
        public ResponseEntity<ErrorResponse> manejarReservaUpdateVaciaException(
                ReservaUpdateVaciaException e) {

                ErrorResponse error = new ErrorResponse(
                        "Solicitud vacía, no válida.",
                        HttpStatus.BAD_REQUEST.value());

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(error);
        }  

        @ExceptionHandler(FechasReservaIncompletasException.class)
        public ResponseEntity<ErrorResponse> manejarFechasReservaIncompletasException(
                FechasReservaIncompletasException e) {

                ErrorResponse error = new ErrorResponse(
                        "Para modificar fechas se debe incluir tanto la de entrada como la de salida.",
                        HttpStatus.BAD_REQUEST.value());

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(error);
        }
        
        @ExceptionHandler(ReservaNoModificableException.class)
        public ResponseEntity<ErrorResponse> manejarReservaNoModificableException(
                ReservaNoModificableException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }

        @ExceptionHandler(HabitacionYaExisteException.class)
        public ResponseEntity<ErrorResponse> manejarHabitacionYaExisteException(
                HabitacionYaExisteException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }

        @ExceptionHandler(CapacidadHabitacionExcedidaException.class)
        public ResponseEntity<ErrorResponse> manejarCapacidadHabitacionExcedidaException(
                CapacidadHabitacionExcedidaException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }

        @ExceptionHandler(ReservaNoConfirmadaException.class)
        public ResponseEntity<ErrorResponse> manejarReservaNoConfirmadaException(
                ReservaNoConfirmadaException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }
    
        @ExceptionHandler(NumPaxNoCoincideException.class)
        public ResponseEntity<ErrorResponse> manejarNumPaxNoCoincideException(
                NumPaxNoCoincideException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }
        
        @ExceptionHandler(ReservaYaOcupadaException.class)
        public ResponseEntity<ErrorResponse> manejarReservaYaOcupadaException(
                ReservaYaOcupadaException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }

        @ExceptionHandler(ImporteIncorrectoException.class)
        public ResponseEntity<ErrorResponse> manejarImporteIncorrectoException(
                ImporteIncorrectoException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }
        
        @ExceptionHandler(ReservaCanceladaException.class)
        public ResponseEntity<ErrorResponse> manejarReservaCanceladaException(
                ReservaCanceladaException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }
 
        @ExceptionHandler(ReservaNoOcupadaException.class)
        public ResponseEntity<ErrorResponse> manejarReservaNoOcupadaException(
                ReservaNoOcupadaException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }
 
        @ExceptionHandler(PagoPendienteException.class)
        public ResponseEntity<ErrorResponse> manejarPagoPendienteException(
                PagoPendienteException e) {

                ErrorResponse error = new ErrorResponse(
                        e.getMessage(),
                        HttpStatus.CONFLICT.value());

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(error);
        }
        
        @ExceptionHandler(FechaReservaInvalidaException.class)
        public ResponseEntity<ErrorResponse> manejarFechaReservaInvalidaException(
                FechaReservaInvalidaException e) {

                ErrorResponse error = new ErrorResponse(
                        "La fecha de salida no puede ser anterior ni igual a la de entrada.",
                        HttpStatus.BAD_REQUEST.value());

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(error);
        }
        
}
