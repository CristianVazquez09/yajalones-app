package com.wolfpack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ViajeRequestDTO {

    private Integer idViaje;
    private String origen;
    private String destino;
    private LocalDateTime fechaSalida;

    private UnidadDTO unidad;



}
