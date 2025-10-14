package com.hospital.gestion.repository;

import com.hospital.gestion.model.AntecedenteMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AntecedenteMedicoRepository extends JpaRepository<AntecedenteMedico, Long> {

    List<AntecedenteMedico> findByHistoriaClinicaIdHistoria(Long idHistoria);

    @Query("SELECT a FROM AntecedenteMedico a WHERE a.historiaClinica.paciente.idPaciente = :idPaciente")
    List<AntecedenteMedico> findByPacienteId(@Param("idPaciente") Long idPaciente);

    List<AntecedenteMedico> findByTipo(String tipo);

    @Query("SELECT a FROM AntecedenteMedico a WHERE a.historiaClinica.idHistoria = :idHistoria AND a.tipo = :tipo")
    List<AntecedenteMedico> findByHistoriaAndTipo(@Param("idHistoria") Long idHistoria, @Param("tipo") String tipo);

    void deleteByHistoriaClinicaIdHistoria(Long idHistoria);
}