package com.sdover.hotelapi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sdover.hotelapi.HotelApiApplication;
import com.sdover.hotelapi.dto.CheckinRequest;
import com.sdover.hotelapi.dto.ReservaRequest;
import com.sdover.hotelapi.dto.ReservaResponse;
import com.sdover.hotelapi.dto.ReservaUpdateRequest;
import com.sdover.hotelapi.exception.ClienteNoEncontradoException;
import com.sdover.hotelapi.exception.FechasReservaIncompletasException;
import com.sdover.hotelapi.exception.FechasReservaInvalidasException;
import com.sdover.hotelapi.exception.HabitacionNoDisponibleException;
import com.sdover.hotelapi.exception.HabitacionNoEncontradaException;
import com.sdover.hotelapi.exception.HotelNoEncontradoException;
import com.sdover.hotelapi.exception.ReservaNoCancelableException;
import com.sdover.hotelapi.exception.ReservaNoEncontradaException;
import com.sdover.hotelapi.exception.ReservaNoModificableException;
import com.sdover.hotelapi.exception.ReservaNoPendienteException;
import com.sdover.hotelapi.exception.ReservaUpdateVaciaException;
import com.sdover.hotelapi.exception.CapacidadHabitacionExcedidaException;
import com.sdover.hotelapi.model.Cliente;
import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.Habitacion;
import com.sdover.hotelapi.model.Hotel;
import com.sdover.hotelapi.model.Reserva;
import com.sdover.hotelapi.model.TipoHabitacion;
import com.sdover.hotelapi.repository.ClienteRepository;
import com.sdover.hotelapi.repository.HabitacionRepository;
import com.sdover.hotelapi.repository.HotelRepository;
import com.sdover.hotelapi.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ClienteService clienteService;
    private final HotelApiApplication hotelApiApplication;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;

    public ReservaService (
        ReservaRepository reservaRepository,
        HabitacionRepository habitacionRepository,
        HotelRepository hotelRepository,
        ClienteRepository clienteRepository, HotelApiApplication hotelApiApplication, ClienteService clienteService
    ) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
        this.hotelApiApplication = hotelApiApplication;
        this.clienteService = clienteService;
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
        List<Habitacion> habitaciones =
                habitacionRepository.findByHotelIdAndTipoHabitacion(
                        hotel.getId(),
                        request.getTipoHabitacion());

        // Comprobar que existen habitaciones de ese tipo
        if (habitaciones.isEmpty()) {
                throw new HabitacionNoDisponibleException(
                        "El hotel no dispone de habitaciones del tipo "
                                + request.getTipoHabitacion());
        }

        // Variable para saber si hemos encontrado alguna habitación libre
        // pero demasiado pequeña para el número de huéspedes
        boolean hayHabitacionSinCapacidad = false;

        // Recorrer las habitaciones candidatas
        for (Habitacion habitacion : habitaciones) {

                boolean ocupada =
                        reservaRepository.existsOverlappingReservation(
                                habitacion.getId(),
                                EstadoReserva.CANCELADA,
                                request.getFechaEntrada(),
                                request.getFechaSalida());

                // Si está ocupada, pasamos a la siguiente habitación
                if (ocupada) {
                continue;
                }

                // La habitación está libre, comprobamos su capacidad
                if (request.getNumPax() > habitacion.getMaxPax()) {
                hayHabitacionSinCapacidad = true;
                continue;
                }

                // Habitación libre y con capacidad suficiente → crear reserva
                Reserva reserva = new Reserva();

                LocalDate fechaEntrada = request.getFechaEntrada();
                LocalDate fechaSalida = request.getFechaSalida();

                Double precioTotal =
                        calcularPrecioTotal(
                                habitacion,
                                fechaEntrada,
                                fechaSalida);

                Integer numPax = request.getNumPax();

                reserva.setHabitacion(habitacion);
                reserva.setFechaCreacion(LocalDateTime.now());
                reserva.setFechaEntrada(fechaEntrada);
                reserva.setFechaSalida(fechaSalida);
                reserva.setPrecioTotal(precioTotal);
                reserva.setCliente(cliente);
                reserva.setNumPax(numPax);
                reserva.setEstadoReserva(EstadoReserva.PENDIENTE);

                Reserva reservaGuardada =
                        reservaRepository.save(reserva);

                return convertirAResponse(reservaGuardada);
        }

        // Hemos recorrido todas las habitaciones y ninguna sirve
        if (hayHabitacionSinCapacidad) {
                throw new CapacidadHabitacionExcedidaException(
                        "No hay ninguna habitación disponible del tipo "
                                + request.getTipoHabitacion()
                                + " con capacidad suficiente para "
                                + request.getNumPax()
                                + " huéspedes.");
        }

        // Hay habitaciones del tipo solicitado, pero todas están ocupadas
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

    @Scheduled(fixedRate = 300000)
    public void caducarReservasPendientes() {

        LocalDateTime limite =
                LocalDateTime.now().minusHours(24);

        List<Reserva> reservasCaducadas =
                reservaRepository.findByEstadoReservaAndFechaCreacionBefore(
                        EstadoReserva.PENDIENTE,
                        limite);

        for (Reserva reserva : reservasCaducadas) {

            reserva.setEstadoReserva(EstadoReserva.CANCELADA);
            reservaRepository.save(reserva);
        }
    }

    public ReservaResponse cancelarReservaConfirmada(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNoEncontradaException(
                                "No existe reserva con id " + id));

        if (reserva.getEstadoReserva() != EstadoReserva.CONFIRMADA) {
            throw new ReservaNoCancelableException(
                    "La reserva con id " + id
                    + " no puede cancelarse porque no está CONFIRMADA.");
        }

        LocalDateTime fechaLimite = reserva.getFechaEntrada()
                .atTime(15, 0)
                .minusHours(48);

        if (LocalDateTime.now().isAfter(fechaLimite)) {
            throw new ReservaNoCancelableException(
                    "La reserva con id " + id
                    + " no puede cancelarse porque han pasado las 48 horas límite.");
        }

        reserva.setEstadoReserva(EstadoReserva.CANCELADA);

        reservaRepository.save(reserva);

        return convertirAResponse(reserva);
    }

    public ReservaResponse actualizarReserva(Long id, ReservaUpdateRequest request) {

        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() ->
                    new ReservaNoEncontradaException(
                            "No existe reserva con id " + id));

        if (reserva.getEstadoReserva() == EstadoReserva.CANCELADA) {
            throw new ReservaNoModificableException(
                    "La reserva con id " + id
                    + " no puede modificarse porque está CANCELADA.");
        }

        if (request.getHotelId() == null
                && request.getTipoHabitacion() == null
                && request.getFechaEntrada() == null
                && request.getFechaSalida() == null
                && request.getClienteId() == null
                && request.getNumPax() == null)  {

            throw new ReservaUpdateVaciaException(
                    "No se ha indicado ningún dato para modificar la reserva.");
        }

        if ((request.getFechaEntrada() == null
                && request.getFechaSalida() != null)
                || (request.getFechaEntrada() != null
                && request.getFechaSalida() == null)) {

            throw new FechasReservaIncompletasException(
                    "Para modificar las fechas de una reserva deben indicarse "
                    + "la fecha de entrada y la fecha de salida.");
        }

        Integer numPaxFinal = request.getNumPax() != null
        ? request.getNumPax()
        : reserva.getNumPax();

        if (reserva.getEstadoReserva() == EstadoReserva.PENDIENTE) {

                boolean cambiaHabitacion = request.getHotelId() != null
                        || request.getTipoHabitacion() != null
                        || request.getFechaEntrada() != null
                        || request.getFechaSalida() != null;

                if (cambiaHabitacion) {

                        Long hotelIdFinal = request.getHotelId() != null
                                ? request.getHotelId()
                                : reserva.getHabitacion().getHotel().getId();

                        TipoHabitacion tipoHabitacionFinal =
                                request.getTipoHabitacion() != null
                                        ? request.getTipoHabitacion()
                                        : reserva.getHabitacion().getTipoHabitacion();

                        LocalDate fechaEntradaFinal =
                                request.getFechaEntrada() != null
                                        ? request.getFechaEntrada()
                                        : reserva.getFechaEntrada();

                        LocalDate fechaSalidaFinal =
                                request.getFechaSalida() != null
                                        ? request.getFechaSalida()
                                        : reserva.getFechaSalida();

                        Habitacion habitacionFinal = buscarHabitacionDisponible(
                                hotelIdFinal,
                                tipoHabitacionFinal,
                                fechaEntradaFinal,
                                fechaSalidaFinal,
                                reserva.getId());

                        if (numPaxFinal > habitacionFinal.getMaxPax()) {
                        throw new CapacidadHabitacionExcedidaException(
                                "La habitación " + habitacionFinal.getNumero()
                                + " admite un máximo de "
                                + habitacionFinal.getMaxPax()
                                + " huéspedes.");
                        }

                        Double precioFinal = calcularPrecioTotal(
                                habitacionFinal,
                                fechaEntradaFinal,
                                fechaSalidaFinal);

                        reserva.setHabitacion(habitacionFinal);
                        reserva.setFechaEntrada(fechaEntradaFinal);
                        reserva.setFechaSalida(fechaSalidaFinal);
                        reserva.setPrecioTotal(precioFinal);

                } else {

                        // No cambia habitación: comprobar capacidad de la habitación actual
                        if (numPaxFinal > reserva.getHabitacion().getMaxPax()) {
                        throw new CapacidadHabitacionExcedidaException(
                                "La habitación " + reserva.getHabitacion().getNumero()
                                + " admite un máximo de "
                                + reserva.getHabitacion().getMaxPax()
                                + " huéspedes.");
                        }
                }

                // Guardar siempre el número final de huéspedes
                reserva.setNumPax(numPaxFinal);

                if (request.getClienteId() != null) {

                        Cliente clienteFinal = clienteRepository.findById(request.getClienteId())
                                .orElseThrow(() ->
                                        new ClienteNoEncontradoException(
                                                "No existe cliente con id "
                                                + request.getClienteId()));

                        reserva.setCliente(clienteFinal);
                }

                reservaRepository.save(reserva);

                return convertirAResponse(reserva);
                

        } else if (reserva.getEstadoReserva() == EstadoReserva.CONFIRMADA) {

                boolean cambiaHabitacion = request.getHotelId() != null
                        || request.getTipoHabitacion() != null
                        || request.getFechaEntrada() != null
                        || request.getFechaSalida() != null;

                // Comprobar regla de 48 h para cambios de fechas u hotel
                if (cambiaHabitacion) {

                        // Asignar valores finales
                        Long hotelIdFinal = request.getHotelId() != null
                                ? request.getHotelId()
                                : reserva.getHabitacion().getHotel().getId();

                        TipoHabitacion tipoHabitacionFinal =
                                request.getTipoHabitacion() != null
                                        ? request.getTipoHabitacion()
                                        : reserva.getHabitacion().getTipoHabitacion();

                        LocalDate fechaEntradaFinal =
                                request.getFechaEntrada() != null
                                        ? request.getFechaEntrada()
                                        : reserva.getFechaEntrada();

                        LocalDate fechaSalidaFinal =
                                request.getFechaSalida() != null
                                        ? request.getFechaSalida()
                                        : reserva.getFechaSalida();

                        // La regla de 48 h solo afecta a cambios de hotel o fechas
                        if (request.getHotelId() != null
                                || request.getFechaEntrada() != null
                                || request.getFechaSalida() != null) {

                        LocalDateTime fechaLimite = reserva.getFechaEntrada()
                                .atTime(15, 0)
                                .minusHours(48);

                        if (LocalDateTime.now().isAfter(fechaLimite)) {
                                throw new ReservaNoModificableException(
                                        "La reserva con id " + id
                                        + " no puede modificarse porque faltan menos de 48 horas "
                                        + "para la fecha de entrada.");
                        }
                        }

                        // Buscar una habitación disponible
                        Habitacion habitacionFinal = buscarHabitacionDisponible(
                                hotelIdFinal,
                                tipoHabitacionFinal,
                                fechaEntradaFinal,
                                fechaSalidaFinal,
                                reserva.getId());

                        // Comprobar que la nueva habitación tiene capacidad suficiente
                        if (numPaxFinal > habitacionFinal.getMaxPax()) {
                        throw new CapacidadHabitacionExcedidaException(
                                "La habitación " + habitacionFinal.getNumero()
                                + " admite un máximo de "
                                + habitacionFinal.getMaxPax()
                                + " huéspedes.");
                        }

                        // Recalcular precio si cambia habitación o fechas
                        Double precioFinal = calcularPrecioTotal(
                                habitacionFinal,
                                fechaEntradaFinal,
                                fechaSalidaFinal);

                        // Asignar los nuevos valores a la reserva
                        reserva.setHabitacion(habitacionFinal);
                        reserva.setFechaEntrada(fechaEntradaFinal);
                        reserva.setFechaSalida(fechaSalidaFinal);
                        reserva.setPrecioTotal(precioFinal);

                } else {

                        // No cambia habitación: comprobar capacidad de la habitación actual
                        if (numPaxFinal > reserva.getHabitacion().getMaxPax()) {
                        throw new CapacidadHabitacionExcedidaException(
                                "La habitación " + reserva.getHabitacion().getNumero()
                                + " admite un máximo de "
                                + reserva.getHabitacion().getMaxPax()
                                + " huéspedes.");
                        }
                }

                // Guardar siempre el número final de huéspedes
                reserva.setNumPax(numPaxFinal);

                // Cambiar cliente si se ha indicado
                if (request.getClienteId() != null) {

                        Cliente clienteFinal = clienteRepository.findById(request.getClienteId())
                                .orElseThrow(() ->
                                        new ClienteNoEncontradoException(
                                                "No existe cliente con id "
                                                + request.getClienteId()));

                        reserva.setCliente(clienteFinal);
                }

                reservaRepository.save(reserva);

                return convertirAResponse(reserva);
                
        } else {

            throw new ReservaNoModificableException("La reserva con id " + id
            + " no puede modificarse en su estado actual.");
        }        
    }

    private Habitacion buscarHabitacionDisponible(
            Long hotelId,
            TipoHabitacion tipoHabitacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            Long reservaId) {

        List<Habitacion> habitacionesDisponibles =
                habitacionRepository.buscarHabitacionDisponible(
                        hotelId,
                        tipoHabitacion,
                        fechaEntrada,
                        fechaSalida,
                        reservaId);

        if (habitacionesDisponibles.isEmpty()) {
            throw new ReservaNoModificableException(
                    "No hay habitaciones disponibles para las condiciones solicitadas.");
        }

        return habitacionesDisponibles.get(0);
    }

    private Double calcularPrecioTotal(
                Habitacion habitacion,
                LocalDate fechaEntrada,
                LocalDate fechaSalida) {

        long noches = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);

        Double precioTotal = noches * habitacion.getPrecioBase();

        return precioTotal;
    }

    // Convertir Reserva -> ReservaResponse
    private ReservaResponse convertirAResponse(Reserva reserva) {

        return new ReservaResponse(
                reserva.getId(),
                reserva.getHabitacion().getHotel().getId(),
                reserva.getHabitacion().getTipoHabitacion(),
                reserva.getFechaCreacion(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getEstadoReserva(),
                reserva.getCliente().getId(),
                reserva.getCliente().getDni(),
                reserva.getNumPax(),
                reserva.getPrecioTotal()
        );
    }

    public void probarDisponibilidad() {

        List<Habitacion> habitacionesDisponibles =
        habitacionRepository.buscarHabitacionDisponible(
                2L,
                TipoHabitacion.DOBLE,
                LocalDate.of(2026, 9, 2),
                LocalDate.of(2026, 9, 10),
                20L
        );

        System.out.println("Habitaciones disponibles: " + habitacionesDisponibles);
    }

    public ReservaResponse hacerCheckin(Long id, CheckinRequest request) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNoEncontradaException(
                                "No existe reserva con id " + id));

        // Aquí iremos añadiendo las comprobaciones del check-in


        return convertirAResponse(reserva);
    }

}
