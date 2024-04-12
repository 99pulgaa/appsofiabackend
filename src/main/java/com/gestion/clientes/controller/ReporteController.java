package com.gestion.clientes.controller;

import com.gestion.clientes.exception.ResourceNotFoundException;
import com.gestion.clientes.modelo.Cliente;
import com.gestion.clientes.modelo.Reporte;
import com.gestion.clientes.repository.ClienteRepository;
import com.gestion.clientes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ReporteController {
    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    //Este pide el archivo pdf
    @GetMapping("/reportes/{id}")
    public ResponseEntity<ByteArrayResource> obtenerDocumento(@PathVariable Long id) {
        Reporte reporte = reporteRepository.findById(id).orElse(null);

        if (reporte != null) {
            ByteArrayResource resource = new ByteArrayResource(reporte.getReporte());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte"
                    + reporte.getId() + ".pdf\"").contentType(MediaType.APPLICATION_PDF).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/reportes/{id}")
    public ResponseEntity<Reporte> actualizarReporte(@PathVariable Long id, @RequestBody Reporte reporteRequest) {
        Reporte reporte = reporteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El reporte con ID " + id + " no existe"));

        reporte.setAuditado(reporteRequest.isAuditado());

        Reporte reporteActualizado = reporteRepository.save(reporte);
        return ResponseEntity.ok(reporteActualizado);
    }

    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarReporte(@PathVariable Long id) {
        Reporte reporte = reporteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El reporte con ID " + id + " no existe"));

        reporteRepository.delete(reporte);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}