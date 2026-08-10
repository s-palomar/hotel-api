package com.sdover.hotelapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.dto.ReservaRequest;
import com.sdover.hotelapi.dto.ReservaResponse;
import com.sdover.hotelapi.exception.ClienteNoEncontradoException;
import com.sdover.hotelapi.exception.FechasReservaInvalidasException;
import com.sdover.hotelapi.exception.HabitacionNoDisponibleException;
import com.sdover.hotelapi.exception.HabitacionNoEncontradaException;
import com.sdover.hotelapi.exception.HotelNoEncontradoException;
import com.sdover.hotelapi.exception.ReservaNoEncontradaException;
import com.sdover.hotelapi.exception.ReservaNoPendienteException;
import com.sdover.hotelapi.model.Cliente;
import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.Habitacion;
import com.sdover.hotelapi.model.Hotel;
import com.sdover.hotelapi.model.Reserva;
import com.sdover.hotelapi.repository.ClienteRepository;
import com.sdover.hotelapi.repository.HabitacionRepository;
import com.sdover.hotelapi.repository.HotelRepository;
import com.sdover.hotelapi.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;

    public ReservaService (
        ReservaRepository reservaRepository,
        HabitacionRepository habitacionRepository,
        HotelRepository hotelRepository,
        ClienteRepository clienteRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
    }

    public ReservaResponse crearReserva(ReservaRequest request) {

        // Validar fechas
        if (!request.getFechaSalida().isAfter(request.getFechaEntrada())) {

            throw new FechasReservaInvalidasException(
                    "La fecha de salida debe ser posterior a la fecha de entrada.");
        }

        // Buscar hotel
        Hotel hotel = hotelRepository.findById(request.getHotelId())
            .orElseThrow(() ->
                new HotelNoEncontradoException(
                        "No existe ningún hotel con id " + request.getHotelId()));

        // Buscar cliente
        Cliente cliente = clienteRepository.findById(request.getClienteId())
            .orElseThrow(() ->
                new ClienteNoEncontradoException(
                        "No existe ningún cliente con id " + request.getClienteId()));


        // Obtener las habitaciones candidatas
        List<Habitacion> habitaciones = habitacionRepository.findByHotelIdAndTipoHabitacion(
                    hotel.getId(),
                    request.getTipoHabitacion());

        // Comprobar que existen habitaciones de ese tipo
        if (habitaciones.isEmpty()) {
            throw new HabitacionNoDisponibleException(
                    "El hotel no dispone de habitaciones del tipo "
                    + request.getTipoHabitacion());
        }

        // Recorrerlas
        for (Habitacion habitacion : habitaciones) {

            boolean ocupada =
                    reservaRepository.existsOverlappingReservation(
                            habitacion.getId(),
                            EstadoReserva.CANCELADA,
                            request.getFechaEntrada(),
                            request.getFechaSalida());
                        
            // Habitación disponible: crear la reserva
            if (!ocupada) {

                Reserva reserva = new Reserva();

                reserva.setHabitacion(habitacion);
                reserva.setFechaEntrada(request.getFechaEntrada());
                reserva.setFechaSalida(request.getFechaSalida());
                reserva.setCliente(cliente);
                reserva.setEstadoReserva(EstadoReserva.PENDIENTE);

                Reserva reservaGuardada = reservaRepository.save(reserva);

                return convertirAResponse(reservaGuardada);                
            }
        }

        // Si no hay habitaciones libres
        throw new HabitacionNoDisponibleException(
            "No hay habitaciones disponibles del tipo "
            + request.getTipoHabitacion()
            + " para las fechas solicitadas.");        
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

    public ReservaResponse confirmarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNoEncontradaException(
                                "No existe reserva con id " + id));

        EstadoReserva estado = reserva.getEstadoReserva();

        if (estado == EstadoReserva.CANCELADA) {

            throw new ReservaNoPendienteException(
                    "La reserva con id " + id
                    + " no puede confirmarse porque está CANCELADA.");
        }

        if (estado == EstadoReserva.CONFIRMADA) {

            throw new ReservaNoPendienteException(
                    "La reserva con id " + id
                    + " ya está CONFIRMADA.");
        }

        reserva.setEstadoReserva(EstadoReserva.CONFIRMADA);

        Reserva reservaConfirmada = reservaRepository.save(reserva);

        return convertirAResponse(reservaConfirmada);
    }

    // Convertir Reserva -> ReservaResponse
    private ReservaResponse convertirAResponse(Reserva reserva) {

        return new ReservaResponse(
                reserva.getId(),
                reserva.getHabitacion().getHotel().getId(),
                reserva.getHabitacion().getTipoHabitacion(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getEstadoReserva(),
                reserva.getCliente().getId(),
                reserva.getCliente().getDni()
        );
    }

}
