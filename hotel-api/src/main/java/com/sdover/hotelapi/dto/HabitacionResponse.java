package com.sdover.hotelapi.dto;

import com.sdover.hotelapi.model.TipoHabitacion;

public class HabitacionResponse {

    private Long id;
    private TipoHabitacion tipoHabitacion;
    private String numero;
    private Double precioBase;
    private Integer maxPax;

    public HabitacionResponse() {}

    public HabitacionResponse (
        Long id,
        TipoHabitacion tipoHabitacion,
        String numero,
        Double precioBase,
        Integer maxPax
    ) {
        this.id = id;
        this.tipoHabitacion = tipoHabitacion;
        this.numero = numero;
        this.precioBase = precioBase;
        this.maxPax = maxPax;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
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

    public Integer getMaxPax() {
        return maxPax;
    }

    public void setMaxPax(Integer maxPax) {
        this.maxPax = maxPax;
    }
}
