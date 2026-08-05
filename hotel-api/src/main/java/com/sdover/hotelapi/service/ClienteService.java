package com.sdover.hotelapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.dto.ClienteRequest;
import com.sdover.hotelapi.dto.ClienteResponse;
import com.sdover.hotelapi.exception.ClienteNoEncontradoException;
import com.sdover.hotelapi.exception.ClienteYaExisteException;
import com.sdover.hotelapi.model.Cliente;
import com.sdover.hotelapi.repository.ClienteRepository;

@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Métodos públicos
    public List<ClienteResponse> obtenerClientes() {

        return clienteRepository.findAll()
            .stream()
            .map(this::convertirAResponse)
            .toList();
    }

    public ClienteResponse obtenerCliente(Long id) {

        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ClienteNoEncontradoException("No existe cliente con id " + id));
        
        return convertirAResponse(cliente);
    }

    public ClienteResponse crearCliente(ClienteRequest request) {

        // Comprobar si ya existe el DNI
        if (clienteRepository.findByDni(request.getDni()).isPresent()) {
            throw new ClienteYaExisteException(
                "Ya existe un cliente con DNI " + request.getDni());
        }

        Cliente cliente = new Cliente();
        cliente.setDni(request.getDni());
        cliente.setNombre(request.getNombre());
        cliente.setApellidos(request.getApellidos());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setNacionalidad(request.getNacionalidad());
        cliente.setFormaPago(request.getFormaPago());

        Cliente clienteGuardado = clienteRepository.save(cliente);
        
        return convertirAResponse(clienteGuardado);
    }

    public ClienteResponse actualizarCliente(Long id, ClienteRequest request) {

        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ClienteNoEncontradoException("No existe cliente con id " + id));

        cliente.setDni(request.getDni());
        cliente.setNombre(request.getNombre());
        cliente.setApellidos(request.getApellidos());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setNacionalidad(request.getNacionalidad());
        cliente.setFormaPago(request.getFormaPago());

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return convertirAResponse(clienteActualizado);
    }

    public void eliminarCliente(Long id) {

        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ClienteNoEncontradoException("No existe cliente con id " + id));

        clienteRepository.delete(cliente);
    }

    // Métodos privados
    private ClienteResponse convertirAResponse(Cliente cliente) {
        
        return new ClienteResponse(
            cliente.getId(),
            cliente.getDni(),
            cliente.getNombre(),
            cliente.getApellidos(),
            cliente.getEmail(),
            cliente.getTelefono(),
            cliente.getNacionalidad(),
            cliente.getFormaPago()
        );
    }
}
