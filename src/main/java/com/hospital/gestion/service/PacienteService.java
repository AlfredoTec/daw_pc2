package com.hospital.gestion.service;

import com.hospital.gestion.dto.PacienteDTO;
import com.hospital.gestion.model.Paciente;
import java.util.List;

public interface PacienteService {
    List<PacienteDTO> findAll();
    PacienteDTO findById(Long id);
    PacienteDTO findByDni(String dni);
    PacienteDTO save(PacienteDTO pacienteDTO);
    PacienteDTO update(Long id, PacienteDTO pacienteDTO);
    void deleteById(Long id);
    void desactivarPaciente(Long id);
    void activarPaciente(Long id);
    List<PacienteDTO> findByEstado(Boolean estado);
    List<PacienteDTO> findByNombre(String nombre);
}