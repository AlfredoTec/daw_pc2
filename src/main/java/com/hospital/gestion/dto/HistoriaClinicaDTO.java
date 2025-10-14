package com.hospital.gestion.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class HistoriaClinicaDTO {
    private Long idHistoria;
    private Long idPaciente;
    private String nombrePaciente;
    private LocalDate fechaApertura;
    private String observaciones;
    private List<AntecedenteMedicoDTO> antecedentes = new ArrayList<>();
}