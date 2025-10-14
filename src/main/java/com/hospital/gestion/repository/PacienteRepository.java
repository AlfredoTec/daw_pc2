package com.hospital.gestion.repository;

import com.hospital.gestion.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByDni(String dni);

    List<Paciente> findByEstadoTrue();

    List<Paciente> findByEstadoFalse();

    @Query("SELECT p FROM Paciente p WHERE p.nombres LIKE %:nombre% OR p.apellidos LIKE %:nombre%")
    List<Paciente> findByNombreContaining(@Param("nombre") String nombre);

    boolean existsByDni(String dni);
}