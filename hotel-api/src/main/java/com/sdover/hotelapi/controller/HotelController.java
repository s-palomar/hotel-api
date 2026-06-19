package com.sdover.hotelapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdover.hotelapi.model.Hotel;
import com.sdover.hotelapi.service.HotelService;

@RestController
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hoteles")
    public List<Hotel> obtenerHoteles() {
        return hotelService.obtenerHoteles();
    }
}
