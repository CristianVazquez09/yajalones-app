package com.wolfpack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Locale;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ViajeDTO {

    private Integer idViaje;
    private String origen;
    private String destino;
    private Locale fechaSalida;

    private UnidadDTO unidad;



}
