package com.sdover.hotelapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdover.hotelapi.model.Habitacion;
import com.sdover.hotelapi.model.TipoHabitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

            List<Habitacion> findByHotelIdAndTipoHabitacion(
                Long hotelId,
                TipoHabitacion tipoHabitacion);
}
