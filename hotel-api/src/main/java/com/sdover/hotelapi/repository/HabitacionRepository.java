package com.sdover.hotelapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sdover.hotelapi.model.Habitacion;
import com.sdover.hotelapi.model.TipoHabitacion;


public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByHotelIdAndTipoHabitacion(
        Long hotelId,
        TipoHabitacion tipoHabitacion);

    boolean existsByHotelIdAndNumero(Long hotelId, String numero);

    @Query("""
        SELECT h
        FROM Habitacion h
        WHERE h.hotel.id = :hotelId
        AND h.tipoHabitacion = :tipoHabitacion
        AND NOT EXISTS (
            SELECT r.id
            FROM Reserva r
            WHERE r.habitacion = h
                AND r.id <> :reservaId
                AND r.estadoReserva <> com.sdover.hotelapi.model.EstadoReserva.CANCELADA
                AND r.fechaEntrada < :fechaSalida
                AND r.fechaSalida > :fechaEntrada
        )
    """)
    List<Habitacion> buscarHabitacionDisponible(
        @Param("hotelId") Long hotelId,
        @Param("tipoHabitacion") TipoHabitacion tipoHabitacion,
        @Param("fechaEntrada") LocalDate fechaEntrada,
        @Param("fechaSalida") LocalDate fechaSalida,
        @Param("reservaId") Long reservaId);
        

    @Query("""
        SELECT CASE WHEN COUNT(r) = 0 THEN true ELSE false END
        FROM Reserva r
        WHERE r.habitacion.id = :habitacionId
        AND r.id <> :reservaId
        AND r.estadoReserva <> com.sdover.hotelapi.model.EstadoReserva.CANCELADA
        AND r.fechaEntrada < :fechaSalida
        AND r.fechaSalida > :fechaEntrada
        """)
    boolean habitacionDisponible(
        @Param("habitacionId") Long habitacionId,
        @Param("fechaEntrada") LocalDate fechaEntrada,
        @Param("fechaSalida") LocalDate fechaSalida,
        @Param("reservaId") Long reservaId);
}
