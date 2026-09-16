package com.sdover.hotelapi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sdover.hotelapi.HotelApiApplication;
import com.sdover.hotelapi.controller.AcompananteController;
import com.sdover.hotelapi.controller.ClienteController;
import com.sdover.hotelapi.dto.AcompananteRequest;
import com.sdover.hotelapi.dto.AcompananteResponse;
import com.sdover.hotelapi.dto.CheckinRequest;
import com.sdover.hotelapi.dto.HabitacionResponse;
import com.sdover.hotelapi.dto.PagoRequest;
import com.sdover.hotelapi.dto.ReservaRequest;
import com.sdover.hotelapi.dto.ReservaResponse;
import com.sdover.hotelapi.dto.ReservaUpdateRequest;
import com.sdover.hotelapi.exception.CapacidadHabitacionExcedidaException;
import com.sdover.hotelapi.exception.CheckinFueraDeFechaException;
import com.sdover.hotelapi.exception.ClienteNoCoincideException;
import com.sdover.hotelapi.exception.ClienteNoEncontradoException;
import com.sdover.hotelapi.exception.FechaReservaInvalidaException;
import com.sdover.hotelapi.exception.FechasReservaIncompletasException;
import com.sdover.hotelapi.exception.FechasReservaInvalidasException;
import com.sdover.hotelapi.exception.HabitacionNoDisponibleException;
import com.sdover.hotelapi.exception.HabitacionNoEncontradaException;
import com.sdover.hotelapi.exception.HotelNoEncontradoException;
import com.sdover.hotelapi.exception.ImporteIncorrectoException;
import com.sdover.hotelapi.exception.NumPaxNoCoincideException;
import com.sdover.hotelapi.exception.PagoPendienteException;
import com.sdover.hotelapi.exception.ReservaCanceladaException;
import com.sdover.hotelapi.exception.ReservaNoCancelableException;
import com.sdover.hotelapi.exception.ReservaNoConfirmadaException;
import com.sdover.hotelapi.exception.ReservaNoEncontradaException;
import com.sdover.hotelapi.exception.ReservaNoModificableException;
import com.sdover.hotelapi.exception.ReservaNoOcupadaException;
import com.sdover.hotelapi.exception.ReservaNoPendienteException;
import com.sdover.hotelapi.exception.ReservaUpdateVaciaException;
import com.sdover.hotelapi.exception.ReservaYaOcupadaException;
import com.sdover.hotelapi.model.Acompanante;
import com.sdover.hotelapi.model.Cliente;
import com.sdover.hotelapi.model.EstadoPago;
import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.Habitacion;
import com.sdover.hotelapi.model.Hotel;
import com.sdover.hotelapi.model.Reserva;
import com.sdover.hotelapi.model.TipoHabitacion;
import com.sdover.hotelapi.repository.AcompananteRepository;
import com.sdover.hotelapi.repository.ClienteRepository;
import com.sdover.hotelapi.repository.HabitacionRepository;
import com.sdover.hotelapi.repository.HotelRepository;
import com.sdover.hotelapi.repository.ReservaRepository;

@Service
public class ReservaService {

    private final AcompananteService acompananteService;
    private final AcompananteController acompananteController;
    private final ClienteController clienteController;
    private final ClienteService clienteService;
    private final HotelApiApplication hotelApiApplication;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;
    private final AcompananteRepository acompananteRepository;

    public ReservaService (
        ReservaRepository reservaRepository,
        HabitacionRepository habitacionRepository,
        HotelRepository hotelRepository,
        ClienteRepository clienteRepository, HotelApiApplication hotelApiApplication, ClienteService clienteService, ClienteController clienteController, AcompananteRepository acompananteRepository, AcompananteController acompananteController, AcompananteService acompananteService
    ) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
        this.hotelApiApplication = hotelApiApplication;
        this.clienteService = clienteService;
        this.clienteController = clienteController;
        this.acompananteRepository = acompananteRepository;
        this.acompananteController = acompananteController;
        this.acompananteService = acompananteService;
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
                reserva.setImportePagado(0.0);
                reserva.setEstadoPago(EstadoPago.PENDIENTE);
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

        if (estado != EstadoReserva.PENDIENTE) {

            throw new ReservaNoPendienteException(
                "La reserva con id " + id
                + " no está PENDIENTE y no puede confirmarse.");
        }

        double pagoMinimo = reserva.getPrecioTotal() * 0.50;

        if (reserva.getImportePagado() < pagoMinimo) {

            throw new ImporteIncorrectoException(
                "La reserva debe tener pagado al menos el 50% para poder confirmarse.");
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

        if (reserva.getEstadoReserva() != EstadoReserva.OCUPADA
                && ((request.getFechaEntrada() == null
                && request.getFechaSalida() != null)
                || (request.getFechaEntrada() != null
                && request.getFechaSalida() == null))) {

                throw new FechasReservaIncompletasException(
                        "Para modificar fechas se debe incluir tanto la de entrada como la de salida.");
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
                
        } else if (reserva.getEstadoReserva() == EstadoReserva.OCUPADA) {

                if (request.getFechaEntrada() != null) {

                        throw new ReservaNoModificableException(
                                "No se puede modificar la fecha de entrada de una reserva OCUPADA.");
                }

                LocalDate fechaEntradaFinal = reserva.getFechaEntrada();

                LocalDate fechaSalidaFinal = request.getFechaSalida() != null
                        ? request.getFechaSalida()
                        : reserva.getFechaSalida();

                // Comprobar que la fecha de salida no es anterior a la fecha de entrada
                if (request.getFechaSalida() != null
                        && !fechaSalidaFinal.isAfter(fechaEntradaFinal)) {

                        throw new FechaReservaInvalidaException(
                        "La fecha de salida debe ser posterior a la fecha de entrada.");
                }

                // Comprobar capacidad con la habitación actual
                if (numPaxFinal > reserva.getHabitacion().getMaxPax()) {

                        throw new CapacidadHabitacionExcedidaException(
                                "La habitación " + reserva.getHabitacion().getNumero()
                                + " admite un máximo de "
                                + reserva.getHabitacion().getMaxPax()
                                + " huéspedes.");
                }

                if (request.getFechaSalida() != null
                        && !request.getFechaSalida().equals(reserva.getFechaSalida())) {

                        // Alargamos salida: Comprobar disponibilidad, recalcular precio
                        if (request.getFechaSalida().isAfter(reserva.getFechaSalida())) {

                                boolean disponible = habitacionDisponible(
                                        reserva.getHabitacion().getId(),
                                        fechaEntradaFinal,
                                        fechaSalidaFinal,
                                        reserva.getId());

                                Habitacion habitacionFinal;

                                if (disponible) {

                                        habitacionFinal = reserva.getHabitacion();

                                } else {

                                        habitacionFinal = buscarHabitacionDisponible(
                                                reserva.getHabitacion().getHotel().getId(),
                                                reserva.getHabitacion().getTipoHabitacion(),
                                                fechaEntradaFinal,
                                                fechaSalidaFinal,
                                                reserva.getId());

                                        if (habitacionFinal == null) {

                                        throw new HabitacionNoDisponibleException(
                                                "No hay ninguna habitación disponible para ampliar la estancia.");
                                        }
                                }

                                // comprobar capacidad de habitacionFinal
                                if (numPaxFinal > habitacionFinal.getMaxPax()) {

                                        throw new CapacidadHabitacionExcedidaException(
                                                "La habitación " + habitacionFinal.getNumero()
                                                + " admite un máximo de "
                                                + habitacionFinal.getMaxPax()
                                                + " huéspedes.");
                                }

                                // Calcular nuevo precio y asignar datos a reserva
                                Double precioFinal = calcularPrecioTotal(
                                        habitacionFinal,
                                        fechaEntradaFinal,
                                        fechaSalidaFinal);

                                reserva.setHabitacion(habitacionFinal);
                                reserva.setFechaSalida(fechaSalidaFinal);
                                reserva.setPrecioTotal(precioFinal); 
                        }                           
                        
                        // Adelantamos salida: No comprobar disponibilidad, solo recalcular precio
                        if (request.getFechaSalida().isBefore(reserva.getFechaSalida())) {

                                Double precioFinal = calcularPrecioTotal(
                                        reserva.getHabitacion(),
                                        fechaEntradaFinal,
                                        fechaSalidaFinal);

                                reserva.setFechaSalida(fechaSalidaFinal);
                                reserva.setPrecioTotal(precioFinal);
                        }
                }

                reserva.setNumPax(numPaxFinal);

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

    private boolean habitacionDisponible(
        Long habitacionId,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        Long reservaId) {

        return habitacionRepository.habitacionDisponible(
                habitacionId,
                fechaEntrada,
                fechaSalida,
                reservaId);
    }

    private Double calcularPrecioTotal(
                Habitacion habitacion,
                LocalDate fechaEntrada,
                LocalDate fechaSalida) {

        long noches = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);

        Double precioTotal = noches * habitacion.getPrecioBase();

        return precioTotal;
    }

    public ReservaResponse hacerCheckin(Long id, CheckinRequest request) {

        // Buscar la reserva y comprobar que existe
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNoEncontradaException(
                                "No existe reserva con id " + id));

        // Comprobar que está confirmada
        if (reserva.getEstadoReserva() == EstadoReserva.OCUPADA) {
                throw new ReservaYaOcupadaException(
                        "El check-in ya se ha realizado para esta reserva");
        }

        if (reserva.getEstadoReserva() == EstadoReserva.CANCELADA) {
                throw new ReservaCanceladaException(
                        "No se puede hacer el check-in de una reserva cancelada");
        }

        if (reserva.getEstadoReserva() != EstadoReserva.CONFIRMADA) {
                throw new ReservaNoConfirmadaException(
                        "Confirmar la reserva antes de hacer el check-in");
        }
                
        // Comprobar fecha de entrada
        LocalDate hoy = LocalDate.now();

        if (hoy.isBefore(reserva.getFechaEntrada()) || !hoy.isBefore(reserva.getFechaSalida())) {
                throw new CheckinFueraDeFechaException(
                                "El check-in solo puede realizarse entre la fecha de entrada y la fecha de salida de la reserva");
        }

        // Comprobar DNI del titular
        if (!request.getClienteDni().equals(reserva.getCliente().getDni())) {
                throw new ClienteNoCoincideException("El DNI introducido no coincide con el DNI de la reserva");
        }

        // Comprobar total huéspedes
        int totalPax = request.getAcompanantes().size() + 1;
        if (totalPax != reserva.getNumPax()) {
                throw new NumPaxNoCoincideException("El nº de huéspedes no coincide con el de la reserva");
        }

        // Comprobar capacidad habitación
        if(totalPax > reserva.getHabitacion().getMaxPax()) {
                throw new CapacidadHabitacionExcedidaException ("El total de huéspedes excede el máximo de ocupantes para esta habitación");
        }

        List<Acompanante> acompanantes = new ArrayList<>();

        // Procesar acompañantes
        for (AcompananteRequest acompananteRequest : request.getAcompanantes()) {
    
                String dni = acompananteRequest.getDni();

                Optional<Cliente> clienteExistente = clienteRepository.findByDni(dni);

                if (clienteExistente.isPresent()) {

                        Cliente cliente = clienteExistente.get();
                        // usamos los datos actuales del Cliente
                        Acompanante acompanante = new Acompanante();

                        acompanante.setDni(cliente.getDni());
                        acompanante.setNombre(cliente.getNombre());
                        acompanante.setApellidos(cliente.getApellidos());
                        acompanante.setEmail(cliente.getEmail());
                        acompanante.setTelefono(cliente.getTelefono());
                        acompanante.setNacionalidad(cliente.getNacionalidad());
                        acompanante.setReserva(reserva);

                        acompanantes.add(acompanante);

                } else {

                        Optional<Acompanante> acompananteExistente = acompananteRepository.findByDni(dni);

                        if (acompananteExistente.isPresent()) {

                                Acompanante acompananteEncontrado = acompananteExistente.get();
                                // usamos los datos que ya conocemos del Acompanante
                                Acompanante acompanante = new Acompanante();

                                acompanante.setDni(acompananteEncontrado.getDni());
                                acompanante.setNombre(acompananteEncontrado.getNombre());
                                acompanante.setApellidos(acompananteEncontrado.getApellidos());
                                acompanante.setEmail(acompananteEncontrado.getEmail());
                                acompanante.setTelefono(acompananteEncontrado.getTelefono());
                                acompanante.setNacionalidad(acompananteEncontrado.getNacionalidad());
                                acompanante.setReserva(reserva);

                                acompanantes.add(acompanante);                        

                        } else {

                        // creamos un Acompanante con los datos del request
                        Acompanante acompanante = new Acompanante();

                        acompanante.setDni(acompananteRequest.getDni());
                        acompanante.setNombre(acompananteRequest.getNombre());
                        acompanante.setApellidos(acompananteRequest.getApellidos());
                        acompanante.setEmail(acompananteRequest.getEmail());
                        acompanante.setTelefono(acompananteRequest.getTelefono());
                        acompanante.setNacionalidad(acompananteRequest.getNacionalidad());
                        acompanante.setReserva(reserva);

                        acompanantes.add(acompanante);    
                        }
                }
        }

        reserva.setAcompanantes(acompanantes);
        reserva.setFechaHoraCheckin(LocalDateTime.now());
        reserva.setEstadoReserva(EstadoReserva.OCUPADA); 

        reservaRepository.save(reserva);

        return convertirAResponse(reserva);
    }

    public ReservaResponse registrarPago(Long id, PagoRequest request) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNoEncontradaException(
                                "No existe reserva con id " + id));

        // Comprobar que la reserva no está CANCELADA ni FINALIZADA
        if (reserva.getEstadoReserva() == EstadoReserva.CANCELADA) {
                throw new ReservaCanceladaException(
                "No se puede registrar un pago para una reserva cancelada.");
        }

        if (reserva.getEstadoReserva() == EstadoReserva.FINALIZADA) {
                throw new ReservaNoModificableException(
                "No se puede registrar un pago para una reserva finalizada.");
        }

        // Comprobar que el importe recibido es válido
        if(request.getImporte() <= 0) {
                throw new ImporteIncorrectoException("El importe debe ser mayor que 0.");
        }

        // Comprobar que el nuevo pago no hace que importePagado supere precioTotal
        if((request.getImporte() + reserva.getImportePagado()) > reserva.getPrecioTotal()) {
                throw new ImporteIncorrectoException("El importe pagado supera el precio total.");
        }

        // Sumar el pago a importePagado
        reserva.setImportePagado(reserva.getImportePagado() + request.getImporte());

        // Asignar estado pago 
        if(reserva.getImportePagado() == 0) {
                reserva.setEstadoPago(EstadoPago.PENDIENTE);
        } else if(reserva.getImportePagado() < reserva.getPrecioTotal()) {
                reserva.setEstadoPago(EstadoPago.PARCIAL);
        } else {
                reserva.setEstadoPago(EstadoPago.COMPLETADO);
        }

        reservaRepository.save(reserva);

        return convertirAResponse(reserva);
    }

    public ReservaResponse hacerCheckout(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNoEncontradaException(
                                "No existe reserva con id " + id));

        EstadoReserva estado = reserva.getEstadoReserva();

        if (estado != EstadoReserva.OCUPADA) {
                throw new ReservaNoOcupadaException(
                        "La reserva con id " + id
                        + " no está OCUPADA y no se puede realizar el checkout.");
        }

        // Comprobar pago total
        if (reserva.getImportePagado() == null
                || reserva.getImportePagado() < reserva.getPrecioTotal()) {

                throw new PagoPendienteException(
                        "No se puede realizar el checkout porque la reserva no está completamente pagada.");
        }

        reserva.setFechaHoraCheckout(LocalDateTime.now());
        reserva.setEstadoReserva(EstadoReserva.FINALIZADA);

        Reserva reservaFinalizada = reservaRepository.save(reserva);

        return convertirAResponse(reservaFinalizada);
    }

    // Realizar check-out automático
    public void finalizarReservasPorFechaSalida() {

        LocalDate hoy = LocalDate.now();

        // Buscar reservas que salen hoy y están CONFIRMADAS u OCUPADAS
        List<Reserva> reservas = reservaRepository
                .findByFechaSalidaAndEstadoReservaIn(
                        hoy,
                        List.of(EstadoReserva.CONFIRMADA, EstadoReserva.OCUPADA));

        for (Reserva reserva : reservas) {

                if (reserva.getEstadoReserva() == EstadoReserva.CONFIRMADA) {

                // No-show
                reserva.setEstadoReserva(EstadoReserva.FINALIZADA);

                } else if (reserva.getEstadoReserva() == EstadoReserva.OCUPADA
                        && reserva.getImportePagado() >= reserva.getPrecioTotal()) {

                // Checkout automático
                reserva.setFechaHoraCheckout(LocalDateTime.now());
                reserva.setEstadoReserva(EstadoReserva.FINALIZADA);
                }
        }

        reservaRepository.saveAll(reservas);
    }

    // Convertir Reserva -> ReservaResponse
    private ReservaResponse convertirAResponse(Reserva reserva) {

        List<AcompananteResponse> acompanantes = reserva.getAcompanantes()
                .stream()
                .map(this::convertirAcompananteResponse)
                .toList();

        HabitacionResponse habitacion = new HabitacionResponse(
                reserva.getHabitacion().getId(),
                reserva.getHabitacion().getTipoHabitacion(),
                reserva.getHabitacion().getNumero(),
                reserva.getHabitacion().getPrecioBase(),
                reserva.getHabitacion().getMaxPax()
        );

        return new ReservaResponse(
                reserva.getId(),
                reserva.getHabitacion().getHotel().getId(),
                reserva.getHabitacion().getTipoHabitacion(),
                habitacion,
                reserva.getFechaCreacion(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                reserva.getEstadoReserva(),
                reserva.getCliente().getId(),
                reserva.getCliente().getDni(),
                reserva.getNumPax(),
                reserva.getPrecioTotal(),
                reserva.getImportePagado(),
                reserva.getEstadoPago(),
                reserva.getFechaHoraCheckin(),
                reserva.getFechaHoraCheckout(),
                acompanantes                
        );
    }

    private AcompananteResponse convertirAcompananteResponse(
        Acompanante acompanante) {

        return new AcompananteResponse(
                acompanante.getId(),
                acompanante.getDni(),
                acompanante.getNombre(),
                acompanante.getApellidos(),
                acompanante.getEmail(),
                acompanante.getTelefono(),
                acompanante.getNacionalidad()
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
}
