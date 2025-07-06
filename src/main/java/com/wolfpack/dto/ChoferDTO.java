package com.wolfpack.dto;

import com.wolfpack.model.Unidad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class ChoferDTO {

    @EqualsAndHashCode.Include
    private Integer idChofer;

    private String nombre;
    private String apellido;
    private String telefono;
    private boolean activo;
    private UnidadDTO unidad;
}
