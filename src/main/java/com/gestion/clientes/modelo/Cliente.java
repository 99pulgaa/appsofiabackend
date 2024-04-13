package com.gestion.clientes.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "clientes")
public class Cliente {
    public enum Rol {
        ADMIN,
        COLABORADOR;
    }

    @Id
    private long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "fechaNacimiento", nullable = false)
    private Date fechaNacimiento;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", columnDefinition = "ENUM('ADMIN', 'COLABORADOR') DEFAULT 'COLABORADOR' CHECK (rol in ('ADMIN', 'COLABORADOR'))")
    private Rol rol;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reporte> reportes;

    @PrePersist
    public void prePersist() {
        if (this.rol == null) {
            this.rol = Rol.COLABORADOR;
        }
    }
}