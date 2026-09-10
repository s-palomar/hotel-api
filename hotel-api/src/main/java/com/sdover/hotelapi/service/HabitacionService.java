package com.sdover.hotelapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.dto.HabitacionRequest;
import com.sdover.hotelapi.dto.HabitacionResponse;
import com.sdover.hotelapi.exception.HabitacionNoEncontradaException;
import com.sdover.hotelapi.exception.HabitacionYaExisteException;
import com.sdover.hotelapi.exception.HotelNoEncontradoException;
import com.sdover.hotelapi.model.Habitacion;
import com.sdover.hotelapi.model.Hotel;
import com.sdover.hotelapi.repository.HabitacionRepository;
import com.sdover.hotelapi.repository.HotelRepository;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HotelRepository hotelRepository;

    public HabitacionService(
        HabitacionRepository habitacionRepository,
        HotelRepository hotelRepository) {

        this.habitacionRepository = habitacionRepository;
        this.hotelRepository = hotelRepository;
    }

    public HabitacionResponse crearHabitacion(Long hotelId, HabitacionRequest request) {

        Hotel hotel = hotelRepository.findById(hotelId)
            .orElseThrow(() -> new HotelNoEncontradoException("No existe ningún hotel con id " + hotelId));

        if (habitacionRepository.existsByHotelIdAndNumero(
                hotelId, request.getNumero())) {

            throw new HabitacionYaExisteException(
                    "Ya existe una habitación con número "
                    + request.getNumero()
                    + " en el hotel con id "
                    + hotelId);
        }
        
        Habitacion habitacion = new Habitacion();

        habitacion.setTipoHabitacion(request.getTipoHabitacion());
        habitacion.setNumero(request.getNumero());
        habitacion.setPrecioBase(request.getPrecioBase());
        habitacion.setHotel(hotel);
        habitacion.setMaxPax(request.getMaxPax());
        
        Habitacion habitacionGuardada = habitacionRepository.save(habitacion);

        return new HabitacionResponse(
            habitacionGuardada.getId(),
            habitacionGuardada.getTipoHabitacion(),
            habitacionGuardada.getNumero(),
            habitacionGuardada.getPrecioBase(),
            habitacionGuardada.getMaxPax()
        );
    }
    
    public List<HabitacionResponse> obtenerHabitaciones() {

        return habitacionRepository.findAll()
            .stream()
            .map(this::convertirAResponse)
            .toList();
    }

    public HabitacionResponse obtenerHabitacion(Long id) {

        Habitacion habitacion = habitacionRepository.findById(id)
            .orElseThrow(() -> new HabitacionNoEncontradaException("No existe habitación con id " + id));

        return convertirAResponse(habitacion);
    }   
    
    public HabitacionResponse actualizarHabitacion(Long id, HabitacionRequest request) {

        Habitacion habitacion = habitacionRepository.findById(id)
            .orElseThrow(() -> new HabitacionNoEncontradaException("No existe habitación con id " + id));
        
        habitacion.setTipoHabitacion(request.getTipoHabitacion());
        habitacion.setNumero(request.getNumero());
        habitacion.setPrecioBase(request.getPrecioBase());

        Habitacion habitacionActualizada = habitacionRepository.save(habitacion);

        return convertirAResponse(habitacionActualizada);
    }

    public void eliminarHabitacion(Long id) {

        Habitacion habitacion = habitacionRepository.findById(id)
            .orElseThrow(() -> new HabitacionNoEncontradaException("No existe habitación con id " + id));

        habitacionRepository.delete(habitacion);
    }

    public List<HabitacionResponse> obtenerHabitacionesHotel(Long hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new HotelNoEncontradoException("No existe ningún hotel con id " + hotelId));

        List<Habitacion> habitaciones = hotel.getHabitaciones();

        return habitaciones.stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // Convertir Habitacion -> HabitacionResponse
    private HabitacionResponse convertirAResponse(Habitacion habitacion) {
        return new HabitacionResponse(
                habitacion.getId(),
                habitacion.getTipoHabitacion(),
                habitacion.getNumero(),
                habitacion.getPrecioBase(),
                habitacion.getMaxPax()
            );
    }
}
