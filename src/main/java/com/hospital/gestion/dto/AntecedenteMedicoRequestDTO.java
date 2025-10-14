package com.hospital.gestion.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AntecedenteMedicoRequestDTO {
    private Long idHistoria;
    private String tipo;
    private String descripcion;
    private LocalDate fechaEvento;
}