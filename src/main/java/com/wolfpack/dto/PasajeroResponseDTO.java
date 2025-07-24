package com.wolfpack.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wolfpack.model.enums.TipoPago;
import com.wolfpack.model.enums.TipoPasajero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PasajeroResponseDTO {

    @EqualsAndHashCode.Include
    private Integer idPasajero;

    private String nombre;
    private String apellido;
    private TipoPasajero tipo;
    private double importe;
    private int asiento;
    private String folio;
    private TipoPago tipoPago;
    @JsonBackReference
    private ViajeResponseDTO viaje;

}
