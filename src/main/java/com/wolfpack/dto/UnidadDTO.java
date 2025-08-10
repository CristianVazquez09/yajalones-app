package com.wolfpack.dto;

import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadDTO {

    @Null(message = "No envíes idUnidad al crear", groups = OnCreate.class)
    @Null(message = "El id de la Unidad debe ir la URL", groups = OnUpdate.class)
    private Integer idUnidad;

    @NotBlank(message = "El nombre es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 10, message = "El nombre admite máximo 10 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String nombre;

    @Size(max = 200, message = "La descripción admite máximo 200 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String descripcion;

    @NotNull(message = "Debes indicar si la unidad está activa", groups = {OnCreate.class, OnUpdate.class})
    private Boolean activo;

    @NotNull(message = "El turno es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private TurnoDTO turno;
}
