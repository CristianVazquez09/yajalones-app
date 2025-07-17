package com.wolfpack.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaqueteDTO {

    private Integer idPaquete;
    private String remitente;
    private String destinatario;
    private double importe;
    private String contenido;
    private UUID folio;
    private boolean posCobrar;
    private boolean estado;
    private ViajeDTO viaje;
}
