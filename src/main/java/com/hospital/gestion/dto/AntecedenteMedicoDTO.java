package com.hospital.gestion.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AntecedenteMedicoDTO {
    private Long idAntecedente;
    private Long idHistoria;
    private String tipo;
    private String descripcion;
    private LocalDate fechaEvento;

    // Método helper para obtener el tipo formateado
    public String getTipoFormateado() {
        switch (tipo) {
            case "alergias": return "Alergias";
            case "enfermedades_previas": return "Enfermedades Previas";
            case "cirugias": return "Cirugías";
            case "enfermedades_familiares": return "Enfermedades Familiares";
            case "medicamentos_cronicos": return "Medicamentos Crónicos";
            case "habitos": return "Hábitos";
            default: return tipo;
        }
    }
}