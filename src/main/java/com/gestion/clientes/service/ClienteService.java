package com.gestion.clientes.service;

import com.gestion.clientes.exception.ResourceNotFoundException;
import com.gestion.clientes.modelo.Cliente;
import com.gestion.clientes.repository.ClienteRepository;
import com.gestion.clientes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ReporteRepository reporteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public ResponseEntity<Cliente> guardarCliente(Cliente cliente) {
        try {
            for (Cliente c : clienteRepository.findAll()) {
                if (c.getEmail().equals(cliente.getEmail()) || c.getId() == cliente.getId()) {
                    throw new ResourceNotFoundException("");
                }
            }

            clienteRepository.save(cliente);
            return ResponseEntity.ok(cliente);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.of(Optional.empty());
        }
    }

    public ResponseEntity<Cliente> listarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));
        return ResponseEntity.ok(cliente);
    }

    public ResponseEntity<Cliente> actualizarCliente(Long id, Cliente clienteRequest) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));

        cliente.setNombre(clienteRequest.getNombre());
        cliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
        cliente.setEmail(clienteRequest.getEmail());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    public ResponseEntity<Cliente> actualizarContrasena(Long id, Cliente clienteRequest) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));

        cliente.setContrasena(clienteRequest.getContrasena());
        Cliente clienteActualizado = clienteRepository.save(cliente);

        return ResponseEntity.ok(clienteActualizado);
    }

    public ResponseEntity<Map<String, Boolean>> eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));
        clienteRepository.delete(cliente);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
