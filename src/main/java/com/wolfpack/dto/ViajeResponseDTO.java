package com.wolfpack.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ViajeResponseDTO {

    private Integer idViaje;
    private String origen;
    private String destino;
    private LocalDateTime fechaSalida;
    private double totalPasajeros;
    private double totalPaqueteria;
    private double comision;
    private double totalPorCobrar;
    private double totalPagadoYajalon;
    private double totalPagadoSclc;
    private double totalPagadoTuxtla;
    private DescuentoDTO descuento;
    private UnidadDTO unidad;
    @JsonManagedReference
    private List<PasajeroResponseDTO> pasajeros;
    @JsonManagedReference
    private List<PaqueteResponseDTO> paquetes;
    private double totalViaje;


}
