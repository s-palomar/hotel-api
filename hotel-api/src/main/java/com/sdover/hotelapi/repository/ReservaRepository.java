package com.sdover.hotelapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.sdover.hotelapi.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

        List<Reserva> findByHabitacionHotelId(Long hotelId);

        List<Reserva> findByHabitacionId(Long habitacionId);

}
