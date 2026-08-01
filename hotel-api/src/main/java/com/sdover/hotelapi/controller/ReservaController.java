package com.sdover.hotelapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdover.hotelapi.dto.ReservaRequest;
import com.sdover.hotelapi.dto.ReservaResponse;
import com.sdover.hotelapi.service.ReservaService;


import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // POST
    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @Valid @RequestBody ReservaRequest request) {

        ReservaResponse reservaCreada = reservaService.crearReserva(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservaCreada);
    }

    // GET /api/reservas
    @GetMapping
    public List<ReservaResponse> obtenerReservas() {

        return reservaService.obtenerReservas();
    }

    // GET /api/reservas/{id}
    @GetMapping("/{id}")
    public ReservaResponse obtenerReserva(@PathVariable Long id) {

        return reservaService.obtenerReserva(id);
    }

    // GET /api/reservas/hotel/{hotelId}
    @GetMapping("/hotel/{hotelId}")
    public List<ReservaResponse> obtenerReservasHotel(
            @PathVariable Long hotelId) {

        return reservaService.obtenerReservasHotel(hotelId);
    }

    // GET /api/reservas/habitacion/{habitacionId}
    @GetMapping("/habitacion/{habitacionId}")
    public List<ReservaResponse> obtenerReservasHabitacion(
            @PathVariable Long habitacionId) {

        return reservaService.obtenerReservasHabitacion(habitacionId);
    }

    // DELETE /api/reservas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {

        reservaService.cancelarReserva(id);

        return ResponseEntity.noContent().build();
    }
}
