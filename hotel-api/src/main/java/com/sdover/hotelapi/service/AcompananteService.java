package com.sdover.hotelapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.dto.AcompananteResponse;
import com.sdover.hotelapi.exception.AcompananteNoEncontradoException;
import com.sdover.hotelapi.model.Acompanante;
import com.sdover.hotelapi.repository.AcompananteRepository;

@Service
public class AcompananteService {

    private final AcompananteRepository acompananteRepository;

    public AcompananteService (AcompananteRepository acompananteRepository) {
        this.acompananteRepository = acompananteRepository;
    }

    // Métodos públicos
    public List<AcompananteResponse> obtenerAcompanantes () {

        return acompananteRepository.findAll()
            .stream()
            .map(this::convertirAResponse)
            .toList();
    }

    public AcompananteResponse obtenerAcompanante(Long id) {

        Acompanante acompanante = acompananteRepository.findById(id)
            .orElseThrow(() -> new AcompananteNoEncontradoException("No existe acompanante con id " + id));
        
        return convertirAResponse(acompanante);
    }

    public AcompananteResponse buscarPorDni(String dni) {
        
        Acompanante acompanante = acompananteRepository.findByDni(dni)
            .orElseThrow(() -> new AcompananteNoEncontradoException("No existe acompanante con DNI " + dni));
        
        return convertirAResponse(acompanante);
    }

    public List<AcompananteResponse> buscarPorApellidos(String apellidos) {

        List<Acompanante> acompanantes =
                acompananteRepository.findByApellidosContainingIgnoreCase(apellidos);

        return acompanantes
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<AcompananteResponse> buscarPorReserva(Long reservaId) {

        List<Acompanante> acompanantes =
                acompananteRepository.findByReservaId(reservaId);

        return acompanantes
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // Métodos privados
    private AcompananteResponse convertirAResponse(Acompanante acompanante) {
        
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

}
