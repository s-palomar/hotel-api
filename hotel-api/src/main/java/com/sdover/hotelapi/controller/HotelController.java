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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdover.hotelapi.dto.HotelRequest;
import com.sdover.hotelapi.dto.HotelResponse;
import com.sdover.hotelapi.model.Hotel;
import com.sdover.hotelapi.service.HotelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hoteles")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {

        this.hotelService = hotelService;

    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> obtenerHotel(@PathVariable Long id) {

        return ResponseEntity.ok(hotelService.obtenerHotel(id));
    }

    @PostMapping
    public ResponseEntity<HotelResponse> crearHotel(@RequestBody HotelRequest request) {

        HotelResponse nuevoHotel = hotelService.crearHotel(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoHotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> actualizarHotel(@PathVariable Long id, @RequestBody Hotel datosActualizados) {

        return ResponseEntity.ok(hotelService.actualizarHotel(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarHotel(@PathVariable Long id) {

        hotelService.borrarHotel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<HotelResponse> obtenerHoteles(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) Integer categoria) {

        if (ciudad != null && categoria != null) {
            return hotelService.buscarPorCiudadYCategoria(ciudad, categoria);

        } else if (ciudad != null) {
            return hotelService.buscarPorCiudad(ciudad);

        } else if (categoria != null) {
            return hotelService.buscarPorCategoria(categoria);

        } else {
            return hotelService.obtenerHoteles();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HotelResponse> actualizarParcialHotel(@PathVariable Long id, @RequestBody Hotel datosParciales) {

        return ResponseEntity.ok(hotelService.actualizarParcialHotel(id, datosParciales));
    }


}   

