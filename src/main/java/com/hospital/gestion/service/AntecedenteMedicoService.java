package com.hospital.gestion.service;

import com.hospital.gestion.dto.AntecedenteMedicoDTO;
import com.hospital.gestion.dto.AntecedenteMedicoRequestDTO;

import java.util.List;

public interface AntecedenteMedicoService {
    AntecedenteMedicoDTO findById(Long id);
    List<AntecedenteMedicoDTO> findByHistoriaClinicaId(Long idHistoria);
    List<AntecedenteMedicoDTO> findByPacienteId(Long idPaciente);
    List<AntecedenteMedicoDTO> findByTipo(String tipo);
    AntecedenteMedicoDTO createAntecedente(AntecedenteMedicoRequestDTO requestDTO);
    AntecedenteMedicoDTO updateAntecedente(Long id, AntecedenteMedicoRequestDTO requestDTO);
    void deleteAntecedente(Long id);
    void deleteAllByHistoriaClinicaId(Long idHistoria);
}