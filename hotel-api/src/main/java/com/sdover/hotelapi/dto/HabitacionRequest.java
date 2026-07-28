package com.sdover.hotelapi.dto;

import com.sdover.hotelapi.model.TipoHabitacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class HabitacionRequest {

    @NotNull(message = "El tipo de habitación es obligatorio") // NotNull para enum
    private TipoHabitacion tipoHabitacion;

    @NotBlank(message = "El número no puede estar vacío") // NotBlank para String
    private String numero;

    @NotNull(message = "El precio no puede estar vacío") // Para Double
    @PositiveOrZero(message = "El precio debe ser 0 ó más")
    private Double precioBase;

    public HabitacionRequest () {}

    public HabitacionRequest (
        TipoHabitacion tipoHabitacion,
        String numero,
        Double precioBase
    ) {
        this.tipoHabitacion = tipoHabitacion;
        this.numero = numero;
        this.precioBase = precioBase;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
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

}
