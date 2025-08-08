package com.wolfpack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Validador de campos combinados (ver abajo)
public class ViajeRequestDTO {

    @EqualsAndHashCode.Include
    @Null(message = "No envíes idViaje al crear", groups = OnCreate.class)
    @Null(message = "El id del Viaje debe ir la URL", groups = OnUpdate.class)
    private Integer idViaje;

    @NotBlank(message = "El origen es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 30, message = "El origen admite máximo 30 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String origen;

    @NotBlank(message = "El destino es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 30, message = "El destino admite máximo 30 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String destino;

    @NotNull(message = "La fecha de salida es obligatoria", groups = {OnCreate.class, OnUpdate.class})
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaSalida;

    @NotNull(message = "La unidad es obligatoria", groups = {OnCreate.class, OnUpdate.class})
    private UnidadDTO unidad;
}
