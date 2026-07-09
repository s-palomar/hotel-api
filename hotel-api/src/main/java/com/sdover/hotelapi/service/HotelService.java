package com.sdover.hotelapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.HotelRepository;
import com.sdover.hotelapi.exception.HotelNoEncontradoException;
import com.sdover.hotelapi.model.Hotel;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService (HotelRepository hotelRepository) {

        this.hotelRepository = hotelRepository;
    }      

    public List<Hotel> obtenerHoteles() {    

        return hotelRepository.findAll(); // findAll devuelve una lista
    }

    public Hotel obtenerHotel(Long id) {

        return hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNoEncontradoException("No existe hotel con id " + id));
    }

    public Hotel crearHotel(Hotel nuevoHotel) {
        
        return hotelRepository.save(nuevoHotel);
    }

    public Hotel actualizarHotel(Long id, Hotel datosActualizados) {

        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new HotelNoEncontradoException("No existe hotel con id " + id));

        hotel.setNombre(datosActualizados.getNombre());
        hotel.setCiudad(datosActualizados.getCiudad());
        hotel.setCategoria(datosActualizados.getCategoria());

        return hotelRepository.save(hotel);
    }

    public void borrarHotel(Long id) {

        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new HotelNoEncontradoException("No existe hotel con id " + id));   
        
        hotelRepository.delete(hotel);         
    }

    public List<Hotel> buscarPorCiudad(String ciudad) {

        List<Hotel> hoteles = hotelRepository.findByCiudad(ciudad);

        if (hoteles.isEmpty()) {
            throw new HotelNoEncontradoException("No existen hoteles en la ciudad " + ciudad);
        }

        return hoteles;
    }

    public List<Hotel> buscarPorCategoria(Integer categoria) {

        List<Hotel> hoteles = hotelRepository.findByCategoria(categoria);

        if (hoteles.isEmpty()) {

            throw new HotelNoEncontradoException("No existen hoteles con la categoria " + categoria);
        }

        return hoteles;
    }

    public List<Hotel> buscarPorCiudadYCategoria(String ciudad, Integer categoria) {

        List<Hotel> hoteles = hotelRepository.findByCiudadAndCategoria(ciudad, categoria);

        if (hoteles.isEmpty()) {

            throw new HotelNoEncontradoException("No existen hoteles en " + ciudad + " con la categoria " + categoria);
        }

        return hoteles;
    }

    public Hotel actualizarParcialHotel(Long id, Hotel datosParciales) {

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

        return hotelRepository.save(hotel);
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
