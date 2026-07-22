package com.sdover.hotelapi.dto;

public class HabitacionResponse {

    private Long id;
    private String numero;
    private Double precioBase;

    public HabitacionResponse() {}

    public HabitacionResponse (
        Long id,
        String numero,
        Double precioBase
    ) {
        this.id = id;
        this.numero = numero;
        this.precioBase = precioBase;
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

}
