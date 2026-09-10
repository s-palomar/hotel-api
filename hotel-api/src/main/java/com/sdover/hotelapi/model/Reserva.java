package com.sdover.hotelapi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCreacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private LocalDateTime fechaHoraCheckin;
    private Integer numPax;
    private Double precioTotal;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoReserva;

    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "reserva")
    private List<Acompanante> acompanantes = new ArrayList<>();

    public Reserva () {}

    public Reserva (
        Long id,
        LocalDateTime fechaCreacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        LocalDateTime fechaHoraCheckin,
        Integer numPax,
        Double precioTotal,
        EstadoReserva estadoReserva,
        Habitacion habitacion,
        Cliente cliente,
        List<Acompanante> acompanantes
    ) {

        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.fechaHoraCheckin = fechaHoraCheckin;
        this.numPax = numPax;
        this.precioTotal = precioTotal;
        this.estadoReserva = estadoReserva;
        this.habitacion = habitacion;
        this.cliente = cliente;
        this.acompanantes = acompanantes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public List<Acompanante> getAcompanantes() {
        return acompanantes;
    }

    public void setAcompanantes(List<Acompanante> acompanantes) {
        this.acompanantes = acompanantes;
    }

}
