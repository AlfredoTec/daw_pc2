package com.hospital.gestion.controller;

import com.hospital.gestion.dto.HistoriaClinicaDTO;
import com.hospital.gestion.dto.HistoriaClinicaRequestDTO;
import com.hospital.gestion.dto.AntecedenteMedicoRequestDTO;
import com.hospital.gestion.service.HistoriaClinicaService;
import com.hospital.gestion.service.PacienteService;
import com.hospital.gestion.service.AntecedenteMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/historias-clinicas")
@RequiredArgsConstructor
public class HistoriaClinicaViewController {

    private final HistoriaClinicaService historiaClinicaService;
    private final PacienteService pacienteService;
    private final AntecedenteMedicoService antecedenteMedicoService;

    @GetMapping("/paciente/{idPaciente}")
    public String verHistoriaClinica(@PathVariable Long idPaciente, Model model) {
        System.out.println("=== INICIANDO VER HISTORIA CLÍNICA ===");
        System.out.println("Paciente ID: " + idPaciente);

        // Primero verificar si existe
        boolean existeHistoria = historiaClinicaService.existsByPacienteId(idPaciente);
        System.out.println("✓ Existe historia clínica: " + existeHistoria);

        if (!existeHistoria) {
            // Crear historia clínica automáticamente si no existe
            return crearHistoriaClinicaSiNoExiste(idPaciente);
        }

        try {
            // Cargar la historia (sin requerir antecedentes)
            System.out.println("Cargando historia clínica...");
            HistoriaClinicaDTO historia = historiaClinicaService.findByPacienteId(idPaciente);
            System.out.println("✓ Historia cargada - ID: " + historia.getIdHistoria());

            // Configurar el modelo
            model.addAttribute("historia", historia);
            model.addAttribute("titulo", "Historia Clínica");
            model.addAttribute("nuevoAntecedente", new AntecedenteMedicoRequestDTO());
            model.addAttribute("tiposAntecedentes", getTiposAntecedentes());

            System.out.println("✓ Mostrando vista ver.html");
            return "historias-clinicas/ver";

        } catch (Exception e) {
            System.out.println("✗ Error cargando historia: " + e.getMessage());
            return "redirect:/";
        }
    }

    private String crearHistoriaClinicaSiNoExiste(Long idPaciente) {
        try {
            System.out.println("Creando historia clínica automáticamente...");
            var paciente = pacienteService.findById(idPaciente);
            System.out.println("✓ Paciente encontrado: " + paciente.getNombres());

            var requestDTO = new HistoriaClinicaRequestDTO();
            requestDTO.setIdPaciente(idPaciente);
            requestDTO.setObservaciones("Historia clínica inicial - complete la información");

            HistoriaClinicaDTO nuevaHistoria = historiaClinicaService.createHistoriaClinica(requestDTO);
            System.out.println("✓ Historia creada - ID: " + nuevaHistoria.getIdHistoria());

            return "redirect:/historias-clinicas/paciente/" + idPaciente;

        } catch (Exception ex) {
            System.out.println("✗ Error creando historia: " + ex.getMessage());
            return "redirect:/";
        }
    }

    // ... los demás métodos se mantienen igual
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            HistoriaClinicaDTO historia = historiaClinicaService.findById(id);
            model.addAttribute("historia", historia);
            model.addAttribute("titulo", "Editar Historia Clínica");
            return "historias-clinicas/formulario";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarHistoriaClinica(@PathVariable Long id,
                                            @RequestParam String observaciones,
                                            RedirectAttributes redirectAttributes) {
        try {
            var requestDTO = new HistoriaClinicaRequestDTO();
            requestDTO.setObservaciones(observaciones);

            HistoriaClinicaDTO historiaActualizada = historiaClinicaService.updateHistoriaClinica(id, requestDTO);
            redirectAttributes.addFlashAttribute("success", "Historia clínica actualizada correctamente");
            return "redirect:/historias-clinicas/paciente/" + historiaActualizada.getIdPaciente();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar historia clínica: " + e.getMessage());
            return "redirect:/historias-clinicas/editar/" + id;
        }
    }

    @PostMapping("/antecedentes/guardar")
    public String guardarAntecedente(@ModelAttribute AntecedenteMedicoRequestDTO antecedenteDTO,
                                     RedirectAttributes redirectAttributes) {
        try {
            antecedenteMedicoService.createAntecedente(antecedenteDTO);
            redirectAttributes.addFlashAttribute("success", "Antecedente médico guardado correctamente");

            var historia = historiaClinicaService.findById(antecedenteDTO.getIdHistoria());
            return "redirect:/historias-clinicas/paciente/" + historia.getIdPaciente();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar antecedente: " + e.getMessage());
            var historia = historiaClinicaService.findById(antecedenteDTO.getIdHistoria());
            return "redirect:/historias-clinicas/paciente/" + historia.getIdPaciente();
        }
    }

    @GetMapping("/antecedentes/eliminar/{id}")
    public String eliminarAntecedente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var antecedente = antecedenteMedicoService.findById(id);
            var historia = historiaClinicaService.findById(antecedente.getIdHistoria());

            antecedenteMedicoService.deleteAntecedente(id);
            redirectAttributes.addFlashAttribute("success", "Antecedente médico eliminado correctamente");
            return "redirect:/historias-clinicas/paciente/" + historia.getIdPaciente();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar antecedente: " + e.getMessage());
            return "redirect:/";
        }
    }

    // Método helper para los tipos de antecedentes
    private List<String> getTiposAntecedentes() {
        return Arrays.asList(
                "alergias",
                "enfermedades_previas",
                "cirugias",
                "enfermedades_familiares",
                "medicamentos_cronicos",
                "habitos",
                "otros"
        );
    }
}