package com.sdover.hotelapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdover.hotelapi.model.Hotel;
import com.sdover.hotelapi.service.HotelService;


@RestController
@RequestMapping("/api/hoteles")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {

        this.hotelService = hotelService;

    }

    @GetMapping
    public List<Hotel> obtenerHoteles() {

        return hotelService.obtenerHoteles();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> obtenerHotel(@PathVariable Long id) {

        Hotel hotel = hotelService.obtenerHotel(id);

        if (hotel != null) {

            return ResponseEntity.ok(hotel);

        } else {

            return ResponseEntity.notFound().build();

        }  

    }

    @PostMapping
    public ResponseEntity<Hotel> crearHotel(@RequestBody Hotel hotel) {

        Hotel nuevoHotel = hotelService.crearHotel(hotel);

        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(nuevoHotel);
    }
    
}
