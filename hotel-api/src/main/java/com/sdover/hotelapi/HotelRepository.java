package com.sdover.hotelapi;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdover.hotelapi.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByCiudad(String ciudad);

    List<Hotel> findByCategoria(int categoria);

    List<Hotel> findByCiudadAndCategoria(String ciudad, int categoria);

}
