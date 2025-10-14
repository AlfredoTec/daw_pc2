package com.hospital.gestion.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "antecedentemedico")
@Data
public class AntecedenteMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAntecedente;

    @ManyToOne
    @JoinColumn(name = "idHistoria", nullable = false)
    private HistoriaClinica historiaClinica;

    @Column(nullable = false, length = 50)
    private String tipo; // alergias, enfermedades_previas, cirugias, etc.

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column
    private LocalDate fechaEvento; // Opcional: fecha cuando ocurrió el antecedente
}