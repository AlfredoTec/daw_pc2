package com.hospital.gestion.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PacienteDTO {
    private Long idPaciente;
    private String dni;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String direccion;
    private String telefono;
    private String correo;
    private Boolean estado;

    // Constructor para nuevos pacientes
    public PacienteDTO() {
        this.estado = true; // Por defecto activo
    }

    // Getter que maneja null
    public Boolean getEstado() {
        return estado != null ? estado : true;
    }
}