package com.sdover.hotelapi.dto;

public class ErrorResponse {

    private String mensaje;
    private int status;

    public ErrorResponse(String mensaje, int status) {
        this.mensaje = mensaje;
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getStatus() {
        return status;
    }
}
