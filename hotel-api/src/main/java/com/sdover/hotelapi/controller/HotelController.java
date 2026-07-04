package com.sdover.hotelapi.controller;

import java.util.List;

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
    public ResponseEntity<Hotel> obtenerHotel(@PathVariable Long id) {

        return ResponseEntity.ok(hotelService.obtenerHotel(id));
    }

    @PostMapping
    public ResponseEntity<Hotel> crearHotel(@Valid @RequestBody Hotel hotel) { // @Valid aplica validaciones @NotBlank de Hotel

        Hotel nuevoHotel = hotelService.crearHotel(hotel);

        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(nuevoHotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> actualizarHotel(@PathVariable Long id, @RequestBody Hotel datosActualizados) {

        return ResponseEntity.ok(hotelService.actualizarHotel(id, datosActualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarHotel(@PathVariable Long id) {

        hotelService.borrarHotel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Hotel> obtenerHoteles(
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

}   

