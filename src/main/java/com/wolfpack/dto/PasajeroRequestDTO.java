package com.wolfpack.dto;

import com.wolfpack.model.enums.TipoPago;
import com.wolfpack.model.enums.TipoPasajero;
import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PasajeroRequestDTO {

    @EqualsAndHashCode.Include
    @Null(message = "No envíes idPasajero al crear", groups = OnCreate.class)
    @Null(message = "El id del Pasajero debe ir la URL", groups = OnUpdate.class)
    private Integer idPasajero;

    @NotBlank(message = "El nombre es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, message = "El nombre admite máximo 50 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, message = "El apellido admite máximo 50 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String apellido;

    @NotNull(message = "El tipo de pasajero es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private TipoPasajero tipo;

    @NotNull(message = "El asiento es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 1, message = "El asiento debe ser mayor o igual a 1", groups = {OnCreate.class, OnUpdate.class})
    private Integer asiento;

    @NotNull(message = "El tipo de pago es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private TipoPago tipoPago;

    @NotNull(message = "Debes indicar el viaje", groups = {OnCreate.class, OnUpdate.class})
    private ViajeRequestDTO viaje;
}
