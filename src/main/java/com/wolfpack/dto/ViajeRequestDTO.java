package com.wolfpack.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ViajeRequestDTO {

    private Integer idViaje;
    private String origen;
    private String destino;
    //private double totalPasajeros;
    //private double totalPaqueteria;
    //private double comision;
    //private double totalPorCobrar;
    //private double totalPagadoYajalon;
    //private double totalPagadoSclc;
    private DescuentoDTO descuento;
    private UnidadDTO unidad;

    //private double totalViaje;
    @JsonManagedReference
    private List<PasajeroDTO> pasajeros;
    @JsonManagedReference
    private List<PaqueteDTO> paquetes;

}
