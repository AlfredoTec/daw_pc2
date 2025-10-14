package com.hospital.gestion.controller;

import com.hospital.gestion.dto.AntecedenteMedicoRequestDTO;
import com.hospital.gestion.service.AntecedenteMedicoService;
import com.hospital.gestion.service.HistoriaClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/antecedentes")
@RequiredArgsConstructor
public class AntecedenteMedicoController {

    private final AntecedenteMedicoService antecedenteMedicoService;
    private final HistoriaClinicaService historiaClinicaService;

    // Método para guardar antecedente (desde el modal)
    @PostMapping("/guardar")
    public String guardarAntecedente(@ModelAttribute AntecedenteMedicoRequestDTO antecedenteDTO,
                                     RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== GUARDANDO ANTECEDENTE ===");
            System.out.println("ID Historia: " + antecedenteDTO.getIdHistoria());
            System.out.println("Tipo: " + antecedenteDTO.getTipo());
            System.out.println("Descripción: " + antecedenteDTO.getDescripcion());

            antecedenteMedicoService.createAntecedente(antecedenteDTO);
            redirectAttributes.addFlashAttribute("success", "Antecedente médico guardado correctamente");

            // Redirigir a la historia clínica del paciente
            var historia = historiaClinicaService.findById(antecedenteDTO.getIdHistoria());
            return "redirect:/historias-clinicas/paciente/" + historia.getIdPaciente();

        } catch (Exception e) {
            System.out.println("✗ Error guardando antecedente: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al guardar antecedente: " + e.getMessage());
            var historia = historiaClinicaService.findById(antecedenteDTO.getIdHistoria());
            return "redirect:/historias-clinicas/paciente/" + historia.getIdPaciente();
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAntecedente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        System.out.println("=== INICIANDO ELIMINACIÓN DE ANTECEDENTE ===");
        System.out.println("ID recibido en controller: " + id);

        try {
            // 1. Verificar que los servicios estén inyectados
            System.out.println("antecedenteMedicoService: " + (antecedenteMedicoService != null ? "OK" : "NULL"));
            System.out.println("historiaClinicaService: " + (historiaClinicaService != null ? "OK" : "NULL"));

            // 2. Buscar el antecedente
            System.out.println("Buscando antecedente con ID: " + id);
            var antecedente = antecedenteMedicoService.findById(id);
            System.out.println("✓ Antecedente encontrado:");
            System.out.println("  - ID Antecedente: " + antecedente.getIdAntecedente());
            System.out.println("  - Tipo: " + antecedente.getTipo());
            System.out.println("  - ID Historia: " + antecedente.getIdHistoria());

            // 3. Buscar la historia clínica
            System.out.println("Buscando historia clínica con ID: " + antecedente.getIdHistoria());
            var historia = historiaClinicaService.findById(antecedente.getIdHistoria());
            System.out.println("✓ Historia encontrada:");
            System.out.println("  - ID Historia: " + historia.getIdHistoria());
            System.out.println("  - ID Paciente: " + historia.getIdPaciente());
            System.out.println("  - Paciente: " + historia.getNombrePaciente());

            // 4. Eliminar el antecedente
            System.out.println("Ejecutando eliminación del antecedente...");
            antecedenteMedicoService.deleteAntecedente(id);
            System.out.println("✓ Eliminación completada en service");

            redirectAttributes.addFlashAttribute("success", "Antecedente médico eliminado correctamente");
            String redirectUrl = "/historias-clinicas/paciente/" + historia.getIdPaciente();
            System.out.println("Redirigiendo a: " + redirectUrl);

            return "redirect:" + redirectUrl;

        } catch (Exception e) {
            System.out.println("✗ ERROR EN CONTROLLER:");
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("error", "Error al eliminar antecedente: " + e.getMessage());
            return "redirect:/";
        }
    }
}