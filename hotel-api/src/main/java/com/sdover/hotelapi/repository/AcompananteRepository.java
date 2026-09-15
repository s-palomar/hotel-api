package com.sdover.hotelapi.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdover.hotelapi.model.Acompanante;

public interface AcompananteRepository extends JpaRepository<Acompanante, Long> {

    Optional<Acompanante> findByDni(String dni);

    List<Acompanante> findByApellidosContainingIgnoreCase(String apellidos);

    List<Acompanante> findByReservaId(Long reservaId);
}
