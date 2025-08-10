package com.wolfpack.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaqueteResponseDTO {

    private Integer idPaquete;
    private String remitente;
    private String destinatario;
    private double importe;
    private String contenido;
    private String folio;
    private boolean porCobrar;
    private boolean estado;
    @JsonBackReference
    private ViajeResponseDTO viaje;
}
