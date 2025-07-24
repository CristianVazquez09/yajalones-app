package com.wolfpack.dto;

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
public class PasajeroDTO {

    @EqualsAndHashCode.Include
    private Integer idPasajero;

    private String nombre;
    private String apellido;
    private TipoPasajero tipo;
    private int asiento;
    private UUID folio;
    private TipoPago tipoPago;
    private ViajeResponseDTO viaje;

}
