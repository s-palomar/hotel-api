package com.sdover.hotelapi.dto;

import java.time.LocalDate;

import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.TipoHabitacion;

public class ReservaResponse {

    private Long id;

    private Long hotelId;

    private TipoHabitacion tipoHabitacion;

    private LocalDate fechaEntrada;

    private LocalDate fechaSalida;

    private EstadoReserva estadoReserva;

    public ReservaResponse() {
    }

    public ReservaResponse(
            Long id,
            Long hotelId,
            TipoHabitacion tipoHabitacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            EstadoReserva estadoReserva) {

        this.id = id;
        this.hotelId = hotelId;
        this.tipoHabitacion = tipoHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estadoReserva = estadoReserva;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }
   
}
