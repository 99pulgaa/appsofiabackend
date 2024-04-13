package com.gestion.clientes.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "reportes")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(name = "reporte", length = 10485760) // Tamaño máximo de 10 MB
    private byte[] reporte;

    @Column(name = "auditado")
    private boolean auditado;
}