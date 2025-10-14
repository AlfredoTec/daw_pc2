package com.hospital.gestion.service;

import com.hospital.gestion.dto.HistoriaClinicaDTO;
import com.hospital.gestion.dto.HistoriaClinicaRequestDTO;
import com.hospital.gestion.dto.AntecedenteMedicoDTO;
import com.hospital.gestion.model.Paciente;

import java.util.List;

public interface HistoriaClinicaService {
    HistoriaClinicaDTO findById(Long id);
    HistoriaClinicaDTO findByPacienteId(Long idPaciente);
    List<AntecedenteMedicoDTO> findAntecedentesByHistoriaId(Long idHistoria); // NUEVO MÉTODO
    HistoriaClinicaDTO createHistoriaClinica(HistoriaClinicaRequestDTO requestDTO);
    HistoriaClinicaDTO updateHistoriaClinica(Long id, HistoriaClinicaRequestDTO requestDTO);
    void deleteHistoriaClinica(Long id);
    void crearHistoriaClinicaAutomatica(Paciente paciente);
    boolean existsByPacienteId(Long idPaciente);
    List<HistoriaClinicaDTO> findAll();
}