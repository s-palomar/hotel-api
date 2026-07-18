package com.sdover.hotelapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private Double precioBase;


    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public Habitacion() {}

    public Habitacion (
        Long id,
        String numero,
        Double precioBase,
        Hotel hotel
    ) {
        this.id = id;
        this.numero = numero;
        this.precioBase = precioBase;
        this.hotel = hotel;
    }

    public Long getId() {
    return id;
}

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public Hotel getHotel() {
    return hotel;
}

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
