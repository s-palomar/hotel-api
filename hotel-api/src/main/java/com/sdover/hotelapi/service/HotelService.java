package com.sdover.hotelapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.HotelRepository;
import com.sdover.hotelapi.dto.HotelRequest;
import com.sdover.hotelapi.dto.HotelResponse;
import com.sdover.hotelapi.exception.HotelNoEncontradoException;
import com.sdover.hotelapi.model.Hotel;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService (HotelRepository hotelRepository) {

        this.hotelRepository = hotelRepository;
    }      

    public HotelResponse crearHotel(HotelRequest request) {

        Hotel hotel = new Hotel();
        hotel.setNombre(request.getNombre());
        hotel.setCiudad(request.getCiudad());
        hotel.setCategoria(request.getCategoria());

        Hotel hotelGuardado = hotelRepository.save(hotel);
        
        return new HotelResponse(
            hotelGuardado.getId(),
            hotelGuardado.getNombre(),
            hotelGuardado.getCiudad(),
            hotelGuardado.getCategoria()
        );
    }

    public List<HotelResponse> obtenerHoteles() {
        
        return hotelRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public HotelResponse obtenerHotel(Long id) {

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNoEncontradoException("No existe hotel con id " + id));

        return convertirAResponse(hotel);
    }

    public HotelResponse actualizarHotel(Long id, Hotel datosActualizados) {

        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new HotelNoEncontradoException("No existe hotel con id " + id));

        hotel.setNombre(datosActualizados.getNombre());
        hotel.setCiudad(datosActualizados.getCiudad());
        hotel.setCategoria(datosActualizados.getCategoria());

        Hotel hotelActualizado = hotelRepository.save(hotel);

        return convertirAResponse(hotelActualizado);
    }

    public void borrarHotel(Long id) {

        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new HotelNoEncontradoException("No existe hotel con id " + id));   
        
        hotelRepository.delete(hotel);         
    }

    public List<HotelResponse> buscarPorCiudad(String ciudad) {

        List<Hotel> hoteles = hotelRepository.findByCiudad(ciudad);

        if (hoteles.isEmpty()) {
            throw new HotelNoEncontradoException("No existen hoteles en la ciudad " + ciudad);
        }

        return hoteles.stream()
            .map(this::convertirAResponse)
            .toList();
    }

    public List<HotelResponse> buscarPorCategoria(Integer categoria) {

        List<Hotel> hoteles = hotelRepository.findByCategoria(categoria);

        if (hoteles.isEmpty()) {

            throw new HotelNoEncontradoException("No existen hoteles con la categoria " + categoria);
        }

        return hoteles.stream()
            .map(this::convertirAResponse)
            .toList();
    }

    public List<HotelResponse> buscarPorCiudadYCategoria(String ciudad, Integer categoria) {

        List<Hotel> hoteles = hotelRepository.findByCiudadAndCategoria(ciudad, categoria);

        if (hoteles.isEmpty()) {

            throw new HotelNoEncontradoException("No existen hoteles en " + ciudad + " con la categoria " + categoria);
        }

        return hoteles.stream()
            .map(this::convertirAResponse)
            .toList();
    }

    public HotelResponse actualizarParcialHotel(Long id, Hotel datosParciales) {

        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new HotelNoEncontradoException("No existe hotel con id " + id));

        if (datosParciales.getNombre() != null) {
            hotel.setNombre(datosParciales.getNombre());
        }

        if (datosParciales.getCiudad() != null) {
            hotel.setCiudad(datosParciales.getCiudad());
        }

        if (datosParciales.getCategoria() != null) {
            hotel.setCategoria(datosParciales.getCategoria());
        }

        Hotel hotelActualizado = hotelRepository.save(hotel);

        return convertirAResponse(hotelActualizado);
    }

    // Convertir Hotel -> HotelResponse
    private HotelResponse convertirAResponse(Hotel hotel) {
        return new HotelResponse(
                hotel.getId(),
                hotel.getNombre(),
                hotel.getCiudad(),
                hotel.getCategoria());
    }

}


/* Antes del repositorio:
    public List<Hotel> obtenerHoteles() {
        return hoteles.stream()
                .filter(hotel -> hotel.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
*/
