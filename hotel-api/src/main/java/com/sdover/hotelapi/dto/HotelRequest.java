package com.sdover.hotelapi.dto;

public class HotelRequest {
    private String nombre;
    private String ciudad;
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
