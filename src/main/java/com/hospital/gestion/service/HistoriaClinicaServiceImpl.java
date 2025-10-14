package com.hospital.gestion.service;

import com.hospital.gestion.dto.HistoriaClinicaDTO;
import com.hospital.gestion.dto.HistoriaClinicaRequestDTO;
import com.hospital.gestion.dto.PacienteDTO;
import com.hospital.gestion.dto.AntecedenteMedicoDTO;
import com.hospital.gestion.model.HistoriaClinica;
import com.hospital.gestion.model.Paciente;
import com.hospital.gestion.model.AntecedenteMedico;
import com.hospital.gestion.repository.HistoriaClinicaRepository;
import com.hospital.gestion.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoriaClinicaServiceImpl implements HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final AntecedenteMedicoService antecedenteMedicoService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public HistoriaClinicaDTO findById(Long id) {
        HistoriaClinica historiaClinica = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + id));
        return convertToDTO(historiaClinica);
    }

    @Override
    @Transactional(readOnly = true)
    public HistoriaClinicaDTO findByPacienteId(Long idPaciente) {
        System.out.println("=== BUSCANDO HISTORIA CLÍNICA POR PACIENTE ID ===");
        System.out.println("ID Paciente: " + idPaciente);

        try {
            // Verificar primero si el paciente existe
            System.out.println("Verificando existencia del paciente...");
            boolean pacienteExiste = pacienteRepository.existsById(idPaciente);
            System.out.println("¿Paciente existe? " + pacienteExiste);

            if (!pacienteExiste) {
                throw new RuntimeException("Paciente no encontrado con ID: " + idPaciente);
            }

            // Buscar la historia clínica CON antecedentes usando JOIN FETCH
            System.out.println("Buscando historia clínica con antecedentes...");
            HistoriaClinica historia = historiaClinicaRepository.findByPacienteIdWithAntecedentes(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada para el paciente con ID: " + idPaciente));

            System.out.println("✓ Historia encontrada con " +
                    (historia.getAntecedentes() != null ? historia.getAntecedentes().size() : 0) +
                    " antecedentes");

            return convertirAHistoriaClinicaDTO(historia);

        } catch (Exception e) {
            System.out.println("✗ Error en findByPacienteId: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Historia clínica no encontrada para el paciente con ID: " + idPaciente);
        }
    }

    // Método opcional si necesitas cargar sin antecedentes
    public HistoriaClinicaDTO findByPacienteIdSinAntecedentes(Long idPaciente) {
        try {
            HistoriaClinica historia = historiaClinicaRepository.findByPacienteIdPaciente(idPaciente)
                    .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada para el paciente con ID: " + idPaciente));

            return convertirAHistoriaClinicaDTO(historia);
        } catch (Exception e) {
            throw new RuntimeException("Historia clínica no encontrada para el paciente con ID: " + idPaciente);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AntecedenteMedicoDTO> findAntecedentesByHistoriaId(Long idHistoria) {
        try {
            System.out.println("Cargando antecedentes para historia ID: " + idHistoria);

            // Usar el servicio de antecedentes que ya tiene la lógica de conversión
            return antecedenteMedicoService.findByHistoriaClinicaId(idHistoria);

        } catch (Exception e) {
            System.out.println("Error cargando antecedentes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public HistoriaClinicaDTO createHistoriaClinica(HistoriaClinicaRequestDTO requestDTO) {
        // Verificar que el paciente existe
        Paciente paciente = pacienteRepository.findById(requestDTO.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + requestDTO.getIdPaciente()));

        // Verificar que no tenga ya una historia clínica
        if (historiaClinicaRepository.existsByPacienteIdPaciente(requestDTO.getIdPaciente())) {
            throw new RuntimeException("El paciente ya tiene una historia clínica registrada");
        }

        HistoriaClinica historiaClinica = new HistoriaClinica();
        historiaClinica.setPaciente(paciente);
        historiaClinica.setObservaciones(requestDTO.getObservaciones());

        if (requestDTO.getFechaApertura() != null) {
            historiaClinica.setFechaApertura(requestDTO.getFechaApertura());
        }

        HistoriaClinica historiaGuardada = historiaClinicaRepository.save(historiaClinica);
        return convertToDTO(historiaGuardada);
    }

    @Override
    @Transactional
    public HistoriaClinicaDTO updateHistoriaClinica(Long id, HistoriaClinicaRequestDTO requestDTO) {
        HistoriaClinica historiaExistente = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + id));

        historiaExistente.setObservaciones(requestDTO.getObservaciones());

        if (requestDTO.getFechaApertura() != null) {
            historiaExistente.setFechaApertura(requestDTO.getFechaApertura());
        }

        HistoriaClinica historiaActualizada = historiaClinicaRepository.save(historiaExistente);
        return convertToDTO(historiaActualizada);
    }

    @Override
    @Transactional
    public void deleteHistoriaClinica(Long id) {
        if (!historiaClinicaRepository.existsById(id)) {
            throw new RuntimeException("Historia clínica no encontrada con ID: " + id);
        }
        historiaClinicaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void crearHistoriaClinicaAutomatica(Paciente paciente) {
        if (!historiaClinicaRepository.existsByPacienteIdPaciente(paciente.getIdPaciente())) {
            HistoriaClinica historiaClinica = new HistoriaClinica();
            historiaClinica.setPaciente(paciente);
            historiaClinica.setObservaciones("Historia clínica creada automáticamente al registrar al paciente");
            historiaClinicaRepository.save(historiaClinica);
        }
    }

    @Override
    public boolean existsByPacienteId(Long idPaciente) {
        return historiaClinicaRepository.existsByPacienteIdPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> findAll() {
        return historiaClinicaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HistoriaClinicaDTO convertToDTO(HistoriaClinica historiaClinica) {
        HistoriaClinicaDTO dto = modelMapper.map(historiaClinica, HistoriaClinicaDTO.class);
        dto.setIdPaciente(historiaClinica.getPaciente().getIdPaciente());
        dto.setNombrePaciente(historiaClinica.getPaciente().getNombres() + " " + historiaClinica.getPaciente().getApellidos());
        return dto;
    }

    // MÉTODO NUEVO CORREGIDO: Conversión manual con soporte para antecedentes (cuando se cargan con JOIN FETCH)
    private HistoriaClinicaDTO convertirAHistoriaClinicaDTO(HistoriaClinica historia) {
        if (historia == null) {
            return null;
        }

        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        dto.setIdHistoria(historia.getIdHistoria());
        dto.setIdPaciente(historia.getPaciente().getIdPaciente());
        dto.setFechaApertura(historia.getFechaApertura());
        dto.setObservaciones(historia.getObservaciones());

        // Solo establecer el nombre del paciente
        if (historia.getPaciente() != null) {
            dto.setNombrePaciente(historia.getPaciente().getNombres() + " " + historia.getPaciente().getApellidos());
        }

        // Ahora podemos cargar antecedentes porque están en la misma sesión (gracias al JOIN FETCH)
        if (historia.getAntecedentes() != null && !historia.getAntecedentes().isEmpty()) {
            System.out.println("Convirtiendo " + historia.getAntecedentes().size() + " antecedentes a DTO");
            List<AntecedenteMedicoDTO> antecedentesDTO = historia.getAntecedentes().stream()
                    .map(this::convertirAAntecedenteMedicoDTO)
                    .collect(Collectors.toList());
            dto.setAntecedentes(antecedentesDTO);
        } else {
            System.out.println("No hay antecedentes para convertir");
            dto.setAntecedentes(new ArrayList<>());
        }

        return dto;
    }

    private AntecedenteMedicoDTO convertirAAntecedenteMedicoDTO(AntecedenteMedico antecedente) {
        AntecedenteMedicoDTO dto = new AntecedenteMedicoDTO();
        dto.setIdAntecedente(antecedente.getIdAntecedente());
        dto.setIdHistoria(antecedente.getHistoriaClinica().getIdHistoria());
        dto.setTipo(antecedente.getTipo());
        dto.setDescripcion(antecedente.getDescripcion());
        dto.setFechaEvento(antecedente.getFechaEvento());
        return dto;
    }
}