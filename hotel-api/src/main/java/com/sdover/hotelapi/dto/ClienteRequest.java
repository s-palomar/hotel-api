package com.sdover.hotelapi.dto;

import com.sdover.hotelapi.model.FormaPago;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ClienteRequest {
    
    @NotBlank
    private String dni;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String telefono;

    @NotBlank
    private String nacionalidad;

    private FormaPago formaPago;

    public ClienteRequest() {}

    public ClienteRequest(
        String dni,
        String nombre,
        String apellidos,
        String email,
        String telefono,
        String nacionalidad,
        FormaPago formaPago
        ) {
            this.dni = dni;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.email = email;
            this.telefono = telefono;
            this.nacionalidad = nacionalidad;
            this.formaPago = formaPago;
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
