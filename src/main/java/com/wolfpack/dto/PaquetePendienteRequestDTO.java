package com.wolfpack.dto;

import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaquetePendienteRequestDTO {

    @EqualsAndHashCode.Include
    @Null(message = "No envíes idPaquete al crear", groups = OnCreate.class)
    @Null(message = "El id del paquete debe ir la URL", groups = OnUpdate.class)
    private Integer idPaquete;

    @NotBlank(message = "El remitente es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 100, message = "Remitente admite máximo 100 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String remitente;

    @NotBlank(message = "El destinatario es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 100, message = "Destinatario admite máximo 100 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String destinatario;

    @NotNull(message = "El importe es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @PositiveOrZero(message = "El importe no puede ser negativo", groups = {OnCreate.class, OnUpdate.class})
    private Double importe;

    @NotBlank(message = "El contenido es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private String contenido;

    @NotNull(message = "Debes indicar si es por cobrar", groups = {OnCreate.class, OnUpdate.class})
    private Boolean porCobrar;

}
