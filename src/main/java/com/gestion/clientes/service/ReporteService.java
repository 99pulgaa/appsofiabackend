package com.gestion.clientes.service;

import com.gestion.clientes.exception.ResourceNotFoundException;
import com.gestion.clientes.modelo.Cliente;
import com.gestion.clientes.modelo.Reporte;
import com.gestion.clientes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public Reporte cargarReporte(MultipartFile archivo, Cliente cliente, boolean auditado) throws IOException {
        byte[] contenido = archivo.getBytes();

        Reporte nuevoReporte = new Reporte();
        nuevoReporte.setCliente(cliente);
        nuevoReporte.setReporte(contenido);
        nuevoReporte.setAuditado(auditado);

        return reporteRepository.save(nuevoReporte);
    }

    // Otros métodos relacionados con la lógica de negocio de los reportes
}
