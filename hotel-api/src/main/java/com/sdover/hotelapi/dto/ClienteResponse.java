package com.sdover.hotelapi.dto;

import com.sdover.hotelapi.model.FormaPago;

public class ClienteResponse {

    private Long id;
    private String dni;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String nacionalidad;
    private FormaPago formaPago;

    public ClienteResponse() {}    

    public ClienteResponse(
        Long id,
        String dni,
        String nombre,
        String apellidos,
        String email,
        String telefono,
        String nacionalidad,
        FormaPago formaPago
        ) {
            this.id = id;
            this.dni = dni;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.email = email;
            this.telefono = telefono;
            this.nacionalidad = nacionalidad;
            this.formaPago = formaPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }



}
