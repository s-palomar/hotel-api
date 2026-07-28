package com.sdover.hotelapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdover.hotelapi.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

}
