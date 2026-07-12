package com.sdover.hotelapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class HotelRequest {
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La ciudad no puede estar vacía")
    private String ciudad;

    @Min(value = 1, message = "La categoría mínima es 1")
    @Max(value = 5, message = "La categoría máxima es 5")
    private Integer categoria;

    public HotelRequest () {}

    public HotelRequest (
        String nombre,
        String ciudad,
        Integer categoria
    ) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }
}
