package com.sdover.hotelapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaController {

    @GetMapping("/hola")
    public String saludar() {
        return "¡Hola desde Spring Boot!";
    }

    @GetMapping("/despedida")
    public String despedida() {
        return "Hasta mañana, Spring!";
    }

}
