package com.sdover.hotelapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class HabitacionRequest {

    @NotBlank(message = "El número no puede estar vacío") // Para String
    private String numero;

    @NotNull(message = "El precio no puede estar vacío") // Para Double
    @PositiveOrZero(message = "El precio debe ser 0 ó más")
    private Double precioBase;

    public HabitacionRequest () {}

    public HabitacionRequest (
        String numero,
        Double precioBase
    ) {
        this.numero = numero;
        this.precioBase = precioBase;
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
