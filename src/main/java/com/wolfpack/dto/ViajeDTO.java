package com.wolfpack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ViajeDTO {

    private Integer idViaje;
    private String origen;
    private String destino;
    private double totalPasajeros;
    private double totalPaqueteria;
    private double comision;
    private double totalPorCobrar;
    private double totalPagadoYajalon;
    private double totalPagadoSclc;
    private DescuentoDTO descuento;
    private UnidadDTO unidad;

}
