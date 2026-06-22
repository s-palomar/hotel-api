package com.sdover.hotelapi.model;

public class Hotel {

    private Long id;
    private String nombre;
    private String ciudad;
    private int categoria;

    public Hotel () {}
    
    public Hotel (
        Long id,
        String nombre,
        String ciudad,
        int categoria
    ) {

        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.categoria = categoria;

    }
    
    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

}
