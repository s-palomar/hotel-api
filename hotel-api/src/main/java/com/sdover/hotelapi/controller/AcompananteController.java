package com.sdover.hotelapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.sdover.hotelapi.dto.AcompananteRequest;
import com.sdover.hotelapi.dto.AcompananteResponse;
import com.sdover.hotelapi.model.Acompanante;
import com.sdover.hotelapi.service.AcompananteService;

@RestController
@RequestMapping("/api/acompanantes")
public class AcompananteController {

    private final AcompananteService acompananteService;

    public AcompananteController(AcompananteService acompananteService) {
        this.acompananteService = acompananteService;
    }

    // GET /api/acompanantes
    @GetMapping
    public List<AcompananteResponse> obtenerAcompanantes() {

        return acompananteService.obtenerAcompanantes();
    }

    // GET /api/acompanantes/{id}
    @GetMapping("/{id}")
    public AcompananteResponse obtenerAcompanante(@PathVariable Long id) {

        return acompananteService.obtenerAcompanante(id);
    }

    // GET /api/acompanantes?dni=12345678A
    @GetMapping(params = "dni")
    public AcompananteResponse buscarPorDni(@RequestParam String dni) {

        return acompananteService.buscarPorDni(dni);
    }

    // GET /api/acompanantes?apellidos=García
    @GetMapping(params = "apellidos")
    public List<AcompananteResponse> buscarPorApellidos(@RequestParam String apellidos)  {

        return acompananteService.buscarPorApellidos(apellidos);
    }

    // GET /api/acompanantes/reserva/{reservaId}
    @GetMapping("/reserva/{reservaId}")
    public List<AcompananteResponse> buscarPorReserva(
            @PathVariable Long reservaId) {

        return acompananteService.buscarPorReserva(reservaId);
    }
}
