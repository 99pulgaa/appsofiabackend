package com.gestion.clientes.controller;

import com.gestion.clientes.exception.ResourceNotFoundException;
import com.gestion.clientes.modelo.Cliente;
import com.gestion.clientes.modelo.Reporte;
import com.gestion.clientes.repository.ClienteRepository;
import com.gestion.clientes.repository.ReporteRepository;
import com.gestion.clientes.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1")
public class ReporteController {
    @Autowired
    private ReporteService reporteService;

    @PutMapping("/clientes/{id}/reportes/{idReporte}")
    public ResponseEntity<Cliente> aprobarReporte(@PathVariable("id") Long id, @PathVariable("idReporte") Long idReporte) {
        return reporteService.aprobarReporte(id, idReporte);
    }

    @PostMapping("/clientes/{id}/anadirReporte")
    public ResponseEntity<Cliente> anadirReporte(@PathVariable("id") Long id, @RequestParam("reporte") MultipartFile archivo) {

        return reporteService.anadirReporte(id, archivo);
    }

    @DeleteMapping("/clientes/{id}/reportes/{idReporte}")
    public ResponseEntity<Cliente> eliminarReporte(@PathVariable("id") Long id, @PathVariable("idReporte") Long idReporte) {
        return reporteService.eliminarReporte(id, idReporte);
    }
}