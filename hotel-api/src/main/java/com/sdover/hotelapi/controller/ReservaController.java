package com.sdover.hotelapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdover.hotelapi.dto.CheckinRequest;
import com.sdover.hotelapi.dto.PagoRequest;
import com.sdover.hotelapi.dto.ReservaRequest;
import com.sdover.hotelapi.dto.ReservaResponse;
import com.sdover.hotelapi.dto.ReservaUpdateRequest;
import com.sdover.hotelapi.service.ReservaService;

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

    // PUT /api/reservas/{id}/confirmar
    @PutMapping("/{id}/confirmar")
    public ReservaResponse confirmarReserva(@PathVariable Long id) {

        return reservaService.confirmarReserva(id);
    }

    // PUT /api/reservas/{id}/cancelar
    @PutMapping("/{id}/cancelar")
    public ReservaResponse cancelarReservaConfirmada(@PathVariable Long id) {

        return reservaService.cancelarReservaConfirmada(id);
    }

    // PATCH /api/reservas/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizarReserva(
            @PathVariable Long id,
            @RequestBody ReservaUpdateRequest request) {

        ReservaResponse reservaActualizada =
                reservaService.actualizarReserva(id, request);

        return ResponseEntity.ok(reservaActualizada);
    }

    // PATCH /api/reservas/{id}/checkin
    @PatchMapping("/{id}/checkin")
    public ResponseEntity<ReservaResponse> hacerCheckin(
            @PathVariable Long id,
            @RequestBody CheckinRequest request) {

        ReservaResponse reserva = reservaService.hacerCheckin(id, request);

        return ResponseEntity.ok(reserva);
    }

    // POST  http://localhost:8080/api/reservas/{id}/pagos
    @PostMapping("/{id}/pagos")
    public ResponseEntity<ReservaResponse> registrarPago(
            @PathVariable Long id,
            @RequestBody PagoRequest request) {

        ReservaResponse reserva = reservaService.registrarPago(id, request);

        return ResponseEntity.ok(reserva);
    }

    // PUT http://localhost:8080/api/reservas/{id}/checkout
    @PutMapping("/{id}/checkout")
    public ResponseEntity<ReservaResponse> hacerCheckout(
            @PathVariable Long id) {

        ReservaResponse reserva = reservaService.hacerCheckout(id);

        return ResponseEntity.ok(reserva);
    }

    // Probar reserva con solapamiento de fechas
    @GetMapping("/probar-disponibilidad")
    public void probarDisponibilidad() {
        reservaService.probarDisponibilidad();
    }

    // Probar finalización automática reserva
    @GetMapping("/probar-finalizacion")
    public ResponseEntity<String> probarFinalizacion() {

        reservaService.finalizarReservasPorFechaSalida();

        return ResponseEntity.ok("Proceso de finalización ejecutado");
    }
    
}
