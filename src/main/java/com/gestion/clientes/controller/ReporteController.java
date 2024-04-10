package com.gestion.clientes.controller;

import com.gestion.clientes.exception.ResourceNotFoundException;
import com.gestion.clientes.modelo.Cliente;
import com.gestion.clientes.modelo.Reporte;
import com.gestion.clientes.repository.ClienteRepository;
import com.gestion.clientes.repository.ReporteRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ReporteController {
    @Autowired
    private ReporteRepository reporteRepository;


    //Este pide el archivo pdf
    @GetMapping("/reportes/{id}")
    public ResponseEntity<ByteArrayResource> obtenerDocumento(@PathVariable Long id) {
        Reporte reporte = reporteRepository.findById(id).orElse(null);

        if (reporte != null) {
            ByteArrayResource resource = new ByteArrayResource(reporte.getReporte());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte" +
                            reporte.getId() + "_" + reporte.getCliente().getId() + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reportes")
    public ResponseEntity<Reporte> cargarReporte(
            @RequestParam("reporte") MultipartFile archivo, // Recibe el archivo PDF
            @ModelAttribute Cliente cliente,               // Recibe el objeto Cliente
            @RequestParam("auditado") boolean auditado) throws IOException {   // Recibe el estado de auditoría del reporte

//        try {
//            // Convierte el archivo a un array de bytes
//            byte[] contenido = archivo.getBytes();
//
//            // Crea una nueva instancia de Reporte y establece sus atributos
//            Reporte nuevoReporte = new Reporte();
//            nuevoReporte.setCliente(cliente); // Asigna el cliente al reporte
//            nuevoReporte.setReporte(contenido); // Guarda el contenido del archivo en el campo de tipo BLOB
//            nuevoReporte.setAuditado(auditado);
//
//            // Guarda el reporte en la base de datos
//            reporteRepository.save(nuevoReporte);
//
//            return ResponseEntity.ok(nuevoReporte);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cargar el reporte: " + e.getMessage());
//        }


        //TODO Arreglar try catch porque el getBytes() en la linea 73 puede fallar y tirar un IoException, ver linea 51
        byte[] contenido = archivo.getBytes();

        // Crea una nueva instancia de Reporte y establece sus atributos
        Reporte nuevoReporte = new Reporte();
        nuevoReporte.setCliente(cliente); // Asigna el cliente al reporte
        nuevoReporte.setReporte(contenido); // Guarda el contenido del archivo en el campo de tipo BLOB
        nuevoReporte.setAuditado(auditado);

        // Guarda el reporte en la base de datos
        reporteRepository.save(nuevoReporte);

        return ResponseEntity.ok(nuevoReporte);
    }

    @PutMapping("/reportes/{id}")
    public ResponseEntity<Reporte> actualizarReporte(@PathVariable Long id, @RequestBody Reporte reporteRequest) {
        Reporte reporte = reporteRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("El reporte con ID " + id + " no existe"));

        reporte.setAuditado(reporteRequest.isAuditado());

        Reporte reporteActualizado = reporteRepository.save(reporte);
        return ResponseEntity.ok(reporteActualizado);
    }

    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarReporte(@PathVariable Long id) {
        Reporte reporte = reporteRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("El reporte con ID " + id + " no existe"));

        reporteRepository.delete(reporte);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}