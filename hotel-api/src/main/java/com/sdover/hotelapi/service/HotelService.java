package com.sdover.hotelapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.model.Hotel;

@Service
public class HotelService {

    public List<Hotel> obtenerHoteles() {

        List<Hotel> hoteles = new ArrayList<>();

        hoteles.add(new Hotel("Hotel Palace", "Madrid", 5));
        hoteles.add(new Hotel("Hotel Atlántico", "A Coruña", 4));
        hoteles.add(new Hotel("Hotel Costa", "Valencia", 3));

        return hoteles;
    }
}
