package com.sdover.hotelapi.dto;

import java.time.LocalDate;

import com.sdover.hotelapi.model.TipoHabitacion;

public class ReservaUpdateRequest {
    
    private Long hotelId;
    private TipoHabitacion tipoHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private Long clienteId;

    public ReservaUpdateRequest () {}

    public ReservaUpdateRequest(Long hotelId,
                                TipoHabitacion tipoHabitacion,
                                LocalDate fechaEntrada,
                                LocalDate fechaSalida,
                                Long clienteId) {
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
