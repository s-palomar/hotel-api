package com.sdover.hotelapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdover.hotelapi.dto.ClienteRequest;
import com.sdover.hotelapi.dto.ClienteResponse;
import com.sdover.hotelapi.model.Cliente;
import com.sdover.hotelapi.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // GET /api/clientes
    @GetMapping
    public List<ClienteResponse> obtenerClientes() {

        return clienteService.obtenerClientes();
    }

    // GET /api/clientes/{id}
    @GetMapping("/{id}")
    public ClienteResponse obtenerCliente(@PathVariable Long id) {

        return clienteService.obtenerCliente(id);
    }

    // POST /api/clientes
    @PostMapping
    public ResponseEntity<ClienteResponse> crearCliente(
            @Valid @RequestBody ClienteRequest request) {

        ClienteResponse clienteCreado = clienteService.crearCliente(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCreado);
    }

    // PUT /api/clientes/{id}
    @PutMapping("/{id}")
    public ClienteResponse actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {

        return clienteService.actualizarCliente(id, request);
    }

    // DELETE /api/clientes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {

        clienteService.eliminarCliente(id);

        return ResponseEntity.noContent().build();
    }
}
