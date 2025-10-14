package com.hospital.gestion.repository;

import com.hospital.gestion.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    Optional<HistoriaClinica> findByPacienteIdPaciente(Long idPaciente);

    // NUEVO MÉTODO: Carga historia clínica con antecedentes usando JOIN FETCH
    @Query("SELECT hc FROM HistoriaClinica hc " +
            "LEFT JOIN FETCH hc.antecedentes " +
            "WHERE hc.paciente.idPaciente = :idPaciente")
    Optional<HistoriaClinica> findByPacienteIdWithAntecedentes(@Param("idPaciente") Long idPaciente);

    boolean existsByPacienteIdPaciente(Long idPaciente);

    @Query("SELECT COUNT(hc) > 0 FROM HistoriaClinica hc WHERE hc.paciente.idPaciente = :idPaciente")
    boolean existsHistoriaByPacienteId(@Param("idPaciente") Long idPaciente);
}