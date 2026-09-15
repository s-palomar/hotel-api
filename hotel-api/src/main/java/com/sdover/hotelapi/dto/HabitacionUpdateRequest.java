package com.sdover.hotelapi.dto;

import com.sdover.hotelapi.model.TipoHabitacion;

public class HabitacionUpdateRequest {

    private String numero;
    private Double precioBase;
    private TipoHabitacion tipoHabitacion;
    private Integer maxPax;

    public HabitacionUpdateRequest() {
    }

    public HabitacionUpdateRequest(String numero, Double precioBase,
                               TipoHabitacion tipoHabitacion, Integer maxPax) {
        this.numero = numero;
        this.precioBase = precioBase;
        this.tipoHabitacion = tipoHabitacion;
        this.maxPax = maxPax;
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

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public Integer getMaxPax() {
        return maxPax;
    }

    public void setMaxPax(Integer maxPax) {
        this.maxPax = maxPax;
    }

}
