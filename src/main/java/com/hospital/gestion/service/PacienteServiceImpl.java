package com.hospital.gestion.service;

import com.hospital.gestion.dto.PacienteDTO;
import com.hospital.gestion.model.Paciente;
import com.hospital.gestion.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaService historiaClinicaService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteDTO> findAll() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteDTO findById(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        return convertToDTO(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteDTO findByDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con DNI: " + dni));
        return convertToDTO(paciente);
    }

    @Override
    @Transactional
    public PacienteDTO save(PacienteDTO pacienteDTO) {
        // Validar que el DNI no exista
        if (pacienteRepository.existsByDni(pacienteDTO.getDni())) {
            throw new RuntimeException("Ya existe un paciente con el DNI: " + pacienteDTO.getDni());
        }

        Paciente paciente = convertToEntity(pacienteDTO);
        paciente.setEstado(true);
        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        // Crear historia clínica automáticamente
        historiaClinicaService.crearHistoriaClinicaAutomatica(pacienteGuardado);

        return convertToDTO(pacienteGuardado);
    }

    @Override
    @Transactional
    public PacienteDTO update(Long id, PacienteDTO pacienteDTO) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));

        // Validar que el DNI no esté en uso por otro paciente
        if (!pacienteExistente.getDni().equals(pacienteDTO.getDni()) &&
                pacienteRepository.existsByDni(pacienteDTO.getDni())) {
            throw new RuntimeException("Ya existe otro paciente con el DNI: " + pacienteDTO.getDni());
        }

        modelMapper.map(pacienteDTO, pacienteExistente);
        pacienteExistente.setIdPaciente(id); // Asegurar que no cambie el ID

        Paciente pacienteActualizado = pacienteRepository.save(pacienteExistente);
        return convertToDTO(pacienteActualizado);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente no encontrado con ID: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void desactivarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        paciente.setEstado(false);
        pacienteRepository.save(paciente);
    }

    @Override
    @Transactional
    public void activarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
        paciente.setEstado(true);
        pacienteRepository.save(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteDTO> findByEstado(Boolean estado) {
        List<Paciente> pacientes = estado ?
                pacienteRepository.findByEstadoTrue() :
                pacienteRepository.findByEstadoFalse();

        return pacientes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteDTO> findByNombre(String nombre) {
        return pacienteRepository.findByNombreContaining(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PacienteDTO convertToDTO(Paciente paciente) {
        return modelMapper.map(paciente, PacienteDTO.class);
    }

    private Paciente convertToEntity(PacienteDTO pacienteDTO) {
        return modelMapper.map(pacienteDTO, Paciente.class);
    }
}