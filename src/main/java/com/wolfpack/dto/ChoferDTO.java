package com.wolfpack.dto;

import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChoferDTO {

    @EqualsAndHashCode.Include
    @Null(message = "No envíes idChofer al crear", groups = OnCreate.class)
    @Null(message = "El id del chofer debe ir la URL", groups = OnUpdate.class)
    private Integer idChofer;

    @NotBlank(message = "El nombre es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, message = "El nombre admite máximo 50 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 50, message = "El apellido admite máximo 50 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String apellido;

    @NotBlank(message = "El teléfono es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "^\\d{10,11}$", message = "El teléfono debe tener 10 a 11 dígitos", groups = {OnCreate.class, OnUpdate.class})
    private String telefono;

    @NotNull(message = "Debes indicar si el chofer está activo", groups = {OnCreate.class, OnUpdate.class})
    private Boolean activo;

    private UnidadDTO unidad;
}
