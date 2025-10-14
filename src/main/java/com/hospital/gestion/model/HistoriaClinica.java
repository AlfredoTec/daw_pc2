package com.hospital.gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "historiaclinica")
@Data
public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistoria;

    @OneToOne
    @JoinColumn(name = "idPaciente", nullable = false, unique = true)
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate fechaApertura;

    @Column(length = 1000)
    private String observaciones;

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AntecedenteMedico> antecedentes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaApertura == null) {
            fechaApertura = LocalDate.now();
        }
    }
}