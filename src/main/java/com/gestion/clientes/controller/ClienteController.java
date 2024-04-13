package com.gestion.clientes.controller;

import com.gestion.clientes.exception.ResourceNotFoundException;
import com.gestion.clientes.modelo.Cliente;
import com.gestion.clientes.modelo.Reporte;
import com.gestion.clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/clientes")
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();

    }

    @PostMapping("/clientes")
    public Cliente guardarCliente(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> ListarClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteRequest) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));

        cliente.setNombre(clienteRequest.getNombre());
        cliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
        cliente.setEmail(clienteRequest.getEmail());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarCliente(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));
        clienteRepository.delete(cliente);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/clientes/{id}/anadirReporte")
    public ResponseEntity<Cliente> anadirReporte(@RequestParam("id") Long id,
                                                 @RequestParam("reporte") MultipartFile archivo,
                                                 @RequestParam("auditado") boolean auditado) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));
        try {

            List<Reporte> reportes = cliente.getReportes();
            if (cliente.getReportes() == null) {
                reportes = new ArrayList<>();
            }

            byte[] contenido = archivo.getBytes();

            // Crea una nueva instancia de Reporte y establece sus atributos
            Reporte nuevoReporte = new Reporte();
            nuevoReporte.setReporte(contenido); // Guarda el contenido del archivo en el campo de tipo BLOB
            nuevoReporte.setAuditado(auditado);
            reportes.add(nuevoReporte);
            cliente.setReportes(reportes);

            clienteRepository.save(cliente);

            return ResponseEntity.ok(cliente);
        } catch (IOException e) {
            System.err.println(e);
            return ResponseEntity.of(Optional.empty());
        }
    }
}
