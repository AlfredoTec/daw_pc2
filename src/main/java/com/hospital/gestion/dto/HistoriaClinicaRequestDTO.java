package com.hospital.gestion.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class HistoriaClinicaRequestDTO {
    private Long idPaciente;
    private String observaciones;
    private LocalDate fechaApertura;
}