package com.hospital.gestion.service;

import com.hospital.gestion.dto.AntecedenteMedicoDTO;
import com.hospital.gestion.dto.AntecedenteMedicoRequestDTO;
import com.hospital.gestion.model.AntecedenteMedico;
import com.hospital.gestion.model.HistoriaClinica;
import com.hospital.gestion.repository.AntecedenteMedicoRepository;
import com.hospital.gestion.repository.HistoriaClinicaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AntecedenteMedicoServiceImpl implements AntecedenteMedicoService {

    private final AntecedenteMedicoRepository antecedenteMedicoRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public AntecedenteMedicoDTO findById(Long id) {
        AntecedenteMedico antecedente = antecedenteMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Antecedente médico no encontrado con ID: " + id));
        return convertToDTO(antecedente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AntecedenteMedicoDTO> findByHistoriaClinicaId(Long idHistoria) {
        return antecedenteMedicoRepository.findByHistoriaClinicaIdHistoria(idHistoria)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AntecedenteMedicoDTO> findByPacienteId(Long idPaciente) {
        return antecedenteMedicoRepository.findByPacienteId(idPaciente)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AntecedenteMedicoDTO> findByTipo(String tipo) {
        return antecedenteMedicoRepository.findByTipo(tipo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AntecedenteMedicoDTO createAntecedente(AntecedenteMedicoRequestDTO requestDTO) {
        // Verificar que la historia clínica existe
        HistoriaClinica historiaClinica = historiaClinicaRepository.findById(requestDTO.getIdHistoria())
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada con ID: " + requestDTO.getIdHistoria()));

        AntecedenteMedico antecedente = new AntecedenteMedico();
        antecedente.setHistoriaClinica(historiaClinica);
        antecedente.setTipo(requestDTO.getTipo());
        antecedente.setDescripcion(requestDTO.getDescripcion());
        antecedente.setFechaEvento(requestDTO.getFechaEvento());

        AntecedenteMedico antecedenteGuardado = antecedenteMedicoRepository.save(antecedente);
        return convertToDTO(antecedenteGuardado);
    }

    @Override
    @Transactional
    public AntecedenteMedicoDTO updateAntecedente(Long id, AntecedenteMedicoRequestDTO requestDTO) {
        AntecedenteMedico antecedenteExistente = antecedenteMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Antecedente médico no encontrado con ID: " + id));

        antecedenteExistente.setTipo(requestDTO.getTipo());
        antecedenteExistente.setDescripcion(requestDTO.getDescripcion());
        antecedenteExistente.setFechaEvento(requestDTO.getFechaEvento());

        AntecedenteMedico antecedenteActualizado = antecedenteMedicoRepository.save(antecedenteExistente);
        return convertToDTO(antecedenteActualizado);
    }

    @Override
    @Transactional
    public void deleteAntecedente(Long id) {
        System.out.println("=== SERVICE: ELIMINANDO ANTECEDENTE ===");
        System.out.println("ID recibido en service: " + id);

        try {
            // Verificar existencia
            System.out.println("Verificando existencia del antecedente...");
            boolean existe = antecedenteMedicoRepository.existsById(id);
            System.out.println("¿Existe antecedente en BD? " + existe);

            if (!existe) {
                System.out.println("✗ Antecedente no encontrado en la base de datos");
                throw new RuntimeException("Antecedente médico no encontrado con ID: " + id);
            }

            // Obtener detalles antes de eliminar
            System.out.println("Obteniendo detalles del antecedente...");
            AntecedenteMedico antecedente = antecedenteMedicoRepository.findById(id).orElse(null);
            if (antecedente != null) {
                System.out.println("Detalles del antecedente:");
                System.out.println("  - ID: " + antecedente.getIdAntecedente());
                System.out.println("  - Tipo: " + antecedente.getTipo());
                System.out.println("  - Descripción: " + antecedente.getDescripcion());
                System.out.println("  - ID Historia: " + (antecedente.getHistoriaClinica() != null ? antecedente.getHistoriaClinica().getIdHistoria() : "NULL"));
            } else {
                System.out.println("✗ No se pudo obtener detalles del antecedente");
            }

            // Eliminar
            System.out.println("Ejecutando deleteById en repository...");
            antecedenteMedicoRepository.deleteById(id);
            System.out.println("✓ deleteById ejecutado");

            // Verificar que se eliminó
            System.out.println("Verificando eliminación...");
            boolean sigueExistiendo = antecedenteMedicoRepository.existsById(id);
            System.out.println("¿Sigue existiendo después de eliminar? " + sigueExistiendo);

            if (sigueExistiendo) {
                System.out.println("✗ El antecedente NO se eliminó correctamente");
            } else {
                System.out.println("✓ Antecedente eliminado correctamente de la BD");
            }

        } catch (Exception e) {
            System.out.println("✗ ERROR EN SERVICE:");
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar antecedente médico: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteAllByHistoriaClinicaId(Long idHistoria) {
        antecedenteMedicoRepository.deleteByHistoriaClinicaIdHistoria(idHistoria);
    }

    private AntecedenteMedicoDTO convertToDTO(AntecedenteMedico antecedente) {
        AntecedenteMedicoDTO dto = modelMapper.map(antecedente, AntecedenteMedicoDTO.class);
        dto.setIdHistoria(antecedente.getHistoriaClinica().getIdHistoria());
        return dto;
    }
}