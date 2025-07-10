package com.wolfpack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class DescuentoDTO {

    @EqualsAndHashCode.Include
    private Integer idDescuento;
    private String concepto;
    private String descripcion;
    private double importe;
}
