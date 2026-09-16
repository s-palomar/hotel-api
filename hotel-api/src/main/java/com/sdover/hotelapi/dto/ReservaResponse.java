package com.sdover.hotelapi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sdover.hotelapi.model.EstadoPago;
import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.TipoHabitacion;

public class ReservaResponse {

    private Long id;
    private Long hotelId;
    private TipoHabitacion tipoHabitacion;
    private HabitacionResponse habitacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private EstadoReserva estadoReserva;
    private Long clienteId;
    private String clienteDni;
    private Integer numPax;
    private Double precioTotal;
    private Double importePagado;
    private EstadoPago estadoPago;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHoraCheckin;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHoraCheckout;

    private List<AcompananteResponse> acompanantes;

    public ReservaResponse() {
    }

    public ReservaResponse(
            Long id,
            Long hotelId,
            TipoHabitacion tipoHabitacion,
            HabitacionResponse habitacion,
            LocalDateTime fechaCreacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            EstadoReserva estadoReserva,
            Long clienteId,
            String clienteDni,
            Integer numPax,
            Double precioTotal,
            Double importePagado,
            EstadoPago estadoPago,
            LocalDateTime fechaHoraCheckin,
            LocalDateTime fechaHoraCheckout,
            List<AcompananteResponse> acompanantes
        ) {

        this.id = id;
        this.hotelId = hotelId;
        this.tipoHabitacion = tipoHabitacion;
        this.habitacion = habitacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estadoReserva = estadoReserva;
        this.clienteId = clienteId;
        this.clienteDni = clienteDni;
        this.numPax = numPax;
        this.precioTotal = precioTotal;
        this.importePagado = importePagado;
        this.estadoPago = estadoPago;
        this.fechaHoraCheckin = fechaHoraCheckin;
        this.fechaHoraCheckout = fechaHoraCheckout;
        this.acompanantes = acompanantes;
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

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Integer getNumPax() {
        return numPax;
    }

    public void setNumPax(Integer numPax) {
        this.numPax = numPax;
    }

    public LocalDateTime getFechaHoraCheckin() {
        return fechaHoraCheckin;
    }

    public void setFechaHoraCheckin(LocalDateTime fechaHoraCheckin) {
        this.fechaHoraCheckin = fechaHoraCheckin;
    }

    public Double getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(Double importePagado) {
        this.importePagado = importePagado;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public List<AcompananteResponse> getAcompanantes() {
        return acompanantes;
    }

    public void setAcompanantes(List<AcompananteResponse> acompanantes) {
        this.acompanantes = acompanantes;
    }

    public LocalDateTime getFechaHoraCheckout() {
        return fechaHoraCheckout;
    }

    public void setFechaHoraCheckout(LocalDateTime fechaHoraCheckout) {
        this.fechaHoraCheckout = fechaHoraCheckout;
    }

    public HabitacionResponse getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(HabitacionResponse habitacion) {
        this.habitacion = habitacion;
    }
}
