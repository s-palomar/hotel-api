package com.sdover.hotelapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

        List<Reserva> findByHabitacionHotelId(Long hotelId);

        List<Reserva> findByHabitacionId(Long habitacionId);


        @Query("""
                SELECT COUNT(r) > 0
                FROM Reserva r
                WHERE r.habitacion.id = :habitacionId
                AND r.estadoReserva <> :estadoCancelada
                AND r.fechaEntrada < :fechaSalida
                AND r.fechaSalida > :fechaEntrada
                """)
        boolean existsOverlappingReservation(
                @Param("habitacionId") Long habitacionId,
                @Param("estadoCancelada") EstadoReserva estadoCancelada,
                @Param("fechaEntrada") LocalDate fechaEntrada,
                @Param("fechaSalida") LocalDate fechaSalida);

}
