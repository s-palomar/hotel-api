package com.sdover.hotelapi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.TipoHabitacion;

public class ReservaResponse {

    private Long id;

    private Long hotelId;

    private TipoHabitacion tipoHabitacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    private LocalDate fechaEntrada;

    private LocalDate fechaSalida;

    private EstadoReserva estadoReserva;

    private Long clienteId;

    private String clienteDni;

    public ReservaResponse() {
    }

    public ReservaResponse(
            Long id,
            Long hotelId,
            TipoHabitacion tipoHabitacion,
            LocalDateTime fechaCreacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            EstadoReserva estadoReserva,
            Long clienteId,
            String clienteDni
        ) {

        this.id = id;
        this.hotelId = hotelId;
        this.tipoHabitacion = tipoHabitacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estadoReserva = estadoReserva;
        this.clienteId = clienteId;
        this.clienteDni = clienteDni;
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteDni() {
        return clienteDni;
    }

    public void setClienteDni(String clienteDni) {
        this.clienteDni = clienteDni;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
