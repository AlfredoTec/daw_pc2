package com.hospital.gestion.controller;

import com.hospital.gestion.dto.PacienteDTO;
import com.hospital.gestion.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PacienteViewController {

    private final PacienteService pacienteService;

    @GetMapping("/")
    public String listarPacientes(Model model) {
        try {
            model.addAttribute("pacientes", pacienteService.findAll());
            model.addAttribute("titulo", "Gestión de Pacientes");
            return "pacientes/listar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar pacientes: " + e.getMessage());
            return "pacientes/listar";
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("paciente", new PacienteDTO());
        model.addAttribute("titulo", "Nuevo Paciente");
        return "pacientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPaciente(@ModelAttribute PacienteDTO pacienteDTO,
                                  RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== GUARDANDO PACIENTE ===");
            System.out.println("Estado recibido: " + pacienteDTO.getEstado());

            pacienteService.save(pacienteDTO);
            redirectAttributes.addFlashAttribute("success", "Paciente guardado correctamente");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar paciente: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            PacienteDTO paciente = pacienteService.findById(id);
            model.addAttribute("paciente", paciente);
            model.addAttribute("titulo", "Editar Paciente");
            return "pacientes/formulario";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarPaciente(@PathVariable Long id,
                                     @ModelAttribute PacienteDTO pacienteDTO,
                                     RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== ACTUALIZANDO PACIENTE ===");
            System.out.println("Estado recibido: " + pacienteDTO.getEstado());

            pacienteService.update(id, pacienteDTO);
            redirectAttributes.addFlashAttribute("success", "Paciente actualizado correctamente");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar paciente: " + e.getMessage());
            return "redirect:/editar/" + id;
        }
    }

    @GetMapping("/ver/{id}")
    public String verPaciente(@PathVariable Long id, Model model) {
        try {
            PacienteDTO paciente = pacienteService.findById(id);
            model.addAttribute("paciente", paciente);
            model.addAttribute("titulo", "Detalles del Paciente");
            return "pacientes/ver";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/desactivar/{id}")
    public String desactivarPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pacienteService.desactivarPaciente(id);
            redirectAttributes.addFlashAttribute("success", "Paciente desactivado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar paciente: " + e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/activar/{id}")
    public String activarPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pacienteService.activarPaciente(id);
            redirectAttributes.addFlashAttribute("success", "Paciente activado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar paciente: " + e.getMessage());
        }
        return "redirect:/";
    }

    // Opcional: eliminar físicamente
    @GetMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pacienteService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Paciente eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar paciente: " + e.getMessage());
        }
        return "redirect:/";
    }
}