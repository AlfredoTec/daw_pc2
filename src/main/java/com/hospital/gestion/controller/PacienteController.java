package com.hospital.gestion.controller;

import com.hospital.gestion.dto.PacienteDTO;
import com.hospital.gestion.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PacienteController {  // Cambié el nombre

    private final PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> getAllPacientes() {
        List<PacienteDTO> pacientes = pacienteService.findAll();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> getPacienteById(@PathVariable Long id) {
        PacienteDTO paciente = pacienteService.findById(id);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteDTO> getPacienteByDni(@PathVariable String dni) {
        PacienteDTO paciente = pacienteService.findByDni(dni);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PacienteDTO>> getPacientesByEstado(@PathVariable Boolean estado) {
        List<PacienteDTO> pacientes = pacienteService.findByEstado(estado);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PacienteDTO>> buscarPacientesPorNombre(@RequestParam String nombre) {
        List<PacienteDTO> pacientes = pacienteService.findByNombre(nombre);
        return ResponseEntity.ok(pacientes);
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> createPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        PacienteDTO nuevoPaciente = pacienteService.save(pacienteDTO);
        return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> updatePaciente(@PathVariable Long id, @Valid @RequestBody PacienteDTO pacienteDTO) {
        PacienteDTO pacienteActualizado = pacienteService.update(id, pacienteDTO);
        return ResponseEntity.ok(pacienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarPaciente(@PathVariable Long id) {
        pacienteService.desactivarPaciente(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activarPaciente(@PathVariable Long id) {
        pacienteService.activarPaciente(id);
        return ResponseEntity.ok().build();
    }
}