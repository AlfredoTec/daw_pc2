package com.hospital.gestion.controller;

import com.hospital.gestion.dto.HistoriaClinicaDTO;
import com.hospital.gestion.dto.HistoriaClinicaRequestDTO;
import com.hospital.gestion.service.HistoriaClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/historias-clinicas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    @GetMapping
    public ResponseEntity<List<HistoriaClinicaDTO>> getAllHistorias() {
        List<HistoriaClinicaDTO> historias = historiaClinicaService.findAll();
        return ResponseEntity.ok(historias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> getHistoriaById(@PathVariable Long id) {
        HistoriaClinicaDTO historia = historiaClinicaService.findById(id);
        return ResponseEntity.ok(historia);
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<HistoriaClinicaDTO> getHistoriaByPaciente(@PathVariable Long idPaciente) {
        HistoriaClinicaDTO historia = historiaClinicaService.findByPacienteId(idPaciente);
        return ResponseEntity.ok(historia);
    }

    @PostMapping
    public ResponseEntity<HistoriaClinicaDTO> createHistoria(@Valid @RequestBody HistoriaClinicaRequestDTO requestDTO) {
        HistoriaClinicaDTO nuevaHistoria = historiaClinicaService.createHistoriaClinica(requestDTO);
        return new ResponseEntity<>(nuevaHistoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriaClinicaDTO> updateHistoria(@PathVariable Long id,
                                                             @Valid @RequestBody HistoriaClinicaRequestDTO requestDTO) {
        HistoriaClinicaDTO historiaActualizada = historiaClinicaService.updateHistoriaClinica(id, requestDTO);
        return ResponseEntity.ok(historiaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoria(@PathVariable Long id) {
        historiaClinicaService.deleteHistoriaClinica(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/paciente/{idPaciente}")
    public ResponseEntity<Boolean> existsByPacienteId(@PathVariable Long idPaciente) {
        boolean exists = historiaClinicaService.existsByPacienteId(idPaciente);
        return ResponseEntity.ok(exists);
    }
}