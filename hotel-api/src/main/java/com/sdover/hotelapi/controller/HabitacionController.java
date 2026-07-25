package com.sdover.hotelapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdover.hotelapi.dto.HabitacionRequest;
import com.sdover.hotelapi.dto.HabitacionResponse;
import com.sdover.hotelapi.dto.HotelResponse;
import com.sdover.hotelapi.service.HabitacionService;

import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    // POST: /api/habitaciones/hotel/{hotelId}
    @PostMapping("/hotel/{hotelId}")
    public ResponseEntity<HabitacionResponse> crearHabitacion(
        @PathVariable Long hotelId,
        @Valid @RequestBody HabitacionRequest request) {

        HabitacionResponse habitacionCreada = habitacionService.crearHabitacion(hotelId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(habitacionCreada);
    }

    // GET: /api/habitaciones/hotel/{hotelId}
    @GetMapping("/hotel/{hotelId}")
    public List<HabitacionResponse> obtenerHabitaciones(
            @PathVariable Long hotelId) {

        return habitacionService.obtenerHabitacionesHotel(hotelId);
    }

    // GET /api/habitaciones/{id}
    @GetMapping("/{id}")
    public HabitacionResponse obtenerHabitacion(@PathVariable Long id) {

        return habitacionService.obtenerHabitacion(id);
    }

    // PUT /api/habitaciones/{id}
    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponse> actualizarHabitacion(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequest request) {

        HabitacionResponse habitacionActualizada =
                habitacionService.actualizarHabitacion(id, request);

        return ResponseEntity.ok(habitacionActualizada);
    }

    // DELETE /api/habitaciones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHabitacion(
            @PathVariable Long id) {

        habitacionService.eliminarHabitacion(id);

        return ResponseEntity.noContent().build();
    }

}
