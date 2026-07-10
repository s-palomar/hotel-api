package com.sdover.hotelapi.dto;

public class HotelResponse {
    private Long id;
    private String nombre;
    private String ciudad;
    private Integer categoria;

    public HotelResponse () {}

    public HotelResponse (
        Long id,
        String nombre,
        String ciudad,
        Integer categoria
    ) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
