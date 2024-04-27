package com.gestion.clientes.service;

import com.gestion.clientes.exception.ResourceNotFoundException;
import com.gestion.clientes.modelo.Cliente;
import com.gestion.clientes.modelo.Reporte;
import com.gestion.clientes.repository.ClienteRepository;
import com.gestion.clientes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {
    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public ResponseEntity<Cliente> aprobarReporte(Long id, Long idReporte) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));

        Reporte reporte = reporteRepository.findById(idReporte).orElseThrow(() -> new ResourceNotFoundException("El reporte con ID " + idReporte + " no existe"));

        try {
            if (cliente.getReportes().contains(reporte)) {
                for (Reporte report : cliente.getReportes()) {
                    if (report.equals(reporte)) {
                        report.setAuditado(true);
                        break;
                    }
                }

                clienteRepository.save(cliente);
            } else {
                throw new Exception("El reporte no pertenece al cliente");
            }

            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.of(Optional.empty());
        }
    }


    public ResponseEntity<Cliente> anadirReporte(Long id, MultipartFile archivo) {
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
            nuevoReporte.setAuditado(false);
            reportes.add(nuevoReporte);
            cliente.setReportes(reportes);

            clienteRepository.save(cliente);

            return ResponseEntity.ok(cliente);
        } catch (IOException e) {
            System.err.println(e);
            return ResponseEntity.of(Optional.empty());
        }
    }

    public ResponseEntity<Cliente> eliminarReporte(Long id, Long idReporte) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El cliente con ese ID no existe : " + id));

        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new ResourceNotFoundException("El reporte con ID no existe : " + idReporte));

        try {
            if (cliente.getReportes().contains(reporte)) {
                cliente.getReportes().remove(reporte);
                clienteRepository.save(cliente);
            } else {
                throw new Exception("El reporte no pertenece al cliente");
            }

            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.of(Optional.empty());
        }
    }
}
