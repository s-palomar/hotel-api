package com.sdover.hotelapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sdover.hotelapi.dto.ClienteRequest;
import com.sdover.hotelapi.dto.ClienteResponse;
import com.sdover.hotelapi.dto.ReservaResponse;
import com.sdover.hotelapi.exception.ClienteNoEncontradoException;
import com.sdover.hotelapi.exception.ClienteTieneReservasException;
import com.sdover.hotelapi.exception.ClienteYaExisteException;
import com.sdover.hotelapi.exception.ClienteDniBloqueadoException;
import com.sdover.hotelapi.model.Cliente;
import com.sdover.hotelapi.model.EstadoReserva;
import com.sdover.hotelapi.model.Reserva;
import com.sdover.hotelapi.repository.ClienteRepository;
import com.sdover.hotelapi.repository.ReservaRepository;

@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final ReservaRepository reservaRepository;

    public ClienteService(ClienteRepository clienteRepository, ReservaRepository reservaRepository) {
        this.clienteRepository = clienteRepository;
        this.reservaRepository = reservaRepository;
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

    public List<ReservaResponse> obtenerReservasCliente(Long clienteId) {

        // Comprobar que el cliente existe
        clienteRepository.findById(clienteId)
            .orElseThrow(() ->
                new ClienteNoEncontradoException(
                    "No existe cliente con id " + clienteId));

        // Obtener las reservas del cliente
        return reservaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirReservaAResponse)
                .toList();
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
        
        // Comprobar si tiene alguna reserva CONFIRMADA
        if (!cliente.getDni().equals(request.getDni())) {

            boolean tieneReservaConfirmada =
                            reservaRepository.existsByClienteIdAndEstadoReserva(
                                id,
                                EstadoReserva.CONFIRMADA);

                    if (tieneReservaConfirmada) {
                        throw new ClienteDniBloqueadoException("No se puede modificar DNI porque el cliente con id " + id + " tiene reservas confirmadas.");
                    }
        }

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
            .orElseThrow(() ->
                new ClienteNoEncontradoException(
                    "No existe cliente con id " + id));

        if (reservaRepository.existsByClienteId(id)) {
            throw new ClienteTieneReservasException(
                "No se puede eliminar el cliente con id "
                + id
                + " porque tiene reservas asociadas.");
        }

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

    private ReservaResponse convertirReservaAResponse(Reserva reserva) {

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
