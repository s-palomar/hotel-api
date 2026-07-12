package com.sdover.hotelapi.dto;

import java.util.Map;

public class ErrorResponse {

    private String mensaje;
    private int status;

    private Map<String, String> errores;

    public ErrorResponse() {} 

    public ErrorResponse(String mensaje, int status) {
        this.mensaje = mensaje;
        this.status = status;
    }

    public ErrorResponse(String mensaje, int status, Map<String, String> errores) {
        this.mensaje = mensaje;
        this.status = status;
        this.errores = errores;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getErrores() {
        return errores;
    }
    
}
