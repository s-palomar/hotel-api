package com.sdover.hotelapi.dto;

import java.time.LocalDate;

import com.sdover.hotelapi.model.TipoHabitacion;

import jakarta.validation.constraints.NotNull;

public class ReservaRequest {

    @NotNull(message = "El id del hotel es obligatorio")
    private Long hotelId;

    @NotNull(message = "El tipo de habitación es obligatorio")
    private TipoHabitacion tipoHabitacion;

    @NotNull(message = "La fecha de entrada es obligatoria")
    private LocalDate fechaEntrada;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDate fechaSalida;

    @NotNull(message = "El id de cliente es obligatorio")
    Long clienteId;

    public ReservaRequest() {
    }

    public ReservaRequest(
            Long hotelId,
            TipoHabitacion tipoHabitacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            Long clienteId
        ) {

        this.hotelId = hotelId;
        this.tipoHabitacion = tipoHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.clienteId = clienteId;
    }
    
    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
}
