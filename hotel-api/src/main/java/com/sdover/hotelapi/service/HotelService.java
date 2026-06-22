package com.sdover.hotelapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.model.Hotel;

@Service
public class HotelService {

    List<Hotel> hoteles = new ArrayList<>();

    public HotelService() {

        // Crear hoteles demo
        hoteles.add(new Hotel(1L, "Hotel Palace", "Madrid", 5));
        hoteles.add(new Hotel(2L, "Hotel Atlántico", "A Coruña", 4));
        hoteles.add(new Hotel(3L, "Hotel Costa", "Valencia", 3));
    
    }

    public List<Hotel> obtenerHoteles() {    

        return hoteles;

    }

    public Hotel obtenerHotel(Long id) {

        return hoteles.stream()
        .filter(hotel -> hotel.getId().equals(id))
        .findFirst()
        .orElse(null);

    }

}
