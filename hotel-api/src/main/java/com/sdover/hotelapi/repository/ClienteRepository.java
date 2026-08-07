package com.sdover.hotelapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdover.hotelapi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDni(String dni);

    List<Cliente> findByApellidosContainingIgnoreCase(String apellidos);

}
