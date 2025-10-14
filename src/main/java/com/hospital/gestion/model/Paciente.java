package com.hospital.gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paciente")
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 1)
    private String sexo; // M, F

    @Column(length = 200)
    private String direccion;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String correo;

    @Column(nullable = false)
    private Boolean estado = true; // true = activo, false = inactivo

    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}