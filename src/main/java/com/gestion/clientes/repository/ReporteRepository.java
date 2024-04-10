package com.gestion.clientes.repository;


import com.gestion.clientes.modelo.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte,Long> {

}
