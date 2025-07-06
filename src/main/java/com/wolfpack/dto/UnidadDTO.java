package com.wolfpack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UnidadDTO {


    @EqualsAndHashCode.Include
    private Integer idUnidad;

    private String nombre;

    private String descripcion;

    private boolean activo;
}
