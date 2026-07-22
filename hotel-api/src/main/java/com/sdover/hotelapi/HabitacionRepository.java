package com.sdover.hotelapi;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdover.hotelapi.model.Habitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {


}
