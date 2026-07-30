package com.sdover.hotelapi.service;

import java.util.List;

import com.sdover.hotelapi.dto.ReservaResponse;
import com.sdover.hotelapi.exception.HabitacionNoEncontradaException;
import com.sdover.hotelapi.exception.HotelNoEncontradoException;
import com.sdover.hotelapi.exception.ReservaNoEncontradaException;
import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.Habitacion;
import com.sdover.hotelapi.model.Reserva;
import com.sdover.hotelapi.model.Hotel;
import com.sdover.hotelapi.repository.HabitacionRepository;
import com.sdover.hotelapi.repository.HotelRepository;
import com.sdover.hotelapi.repository.ReservaRepository;

public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final HotelRepository hotelRepository;

    public ReservaService (
        ReservaRepository reservaRepository,
        HabitacionRepository habitacionRepository,
        HotelRepository hotelRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.hotelRepository = hotelRepository;

    }

    public ReservaResponse obtenerReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNoEncontradaException(
                                "No existe reserva con id " + id));

        return convertirAResponse(reserva);
    }

    public List<ReservaResponse> obtenerReservas() {

        return reservaRepository.findAll()
            .stream()
            .map(this::convertirAResponse)
            .toList();
    }

    public List<ReservaResponse> obtenerReservasHotel(Long hotelId) {       

        // Comprobar que existe el hotel
        hotelRepository.findById(hotelId)
            .orElseThrow(() ->
                new HotelNoEncontradoException(
                    "No existe ningún hotel con id " + hotelId));

        // Obtener todas las reservas del hotel
        return reservaRepository.findByHabitacionHotelId(hotelId)
            .stream()
            .map(this::convertirAResponse)
            .toList();        
    }

    public List<ReservaResponse> obtenerReservasHabitacion(Long habitacionId) {

        habitacionRepository.findById(habitacionId)
                .orElseThrow(() ->
                        new HabitacionNoEncontradaException(
                                "No existe ninguna habitación con id " + habitacionId));

        return reservaRepository.findByHabitacionId(habitacionId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public void cancelarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNoEncontradaException(
                                "No existe reserva con id " + id));

        reserva.setEstadoReserva(EstadoReserva.CANCELADA);

        reservaRepository.save(reserva);
    }

    // Convertir Reserva -> ReservaResponse
    private ReservaResponse convertirAResponse(Reserva reserva) {

        return new ReservaResponse(
                reserva.getId(),
                reserva.getHabitacion().getHotel().getId(),
                reserva.getHabitacion().getTipoHabitacion(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getEstadoReserva()
        );
    }

}
