package com.wolfpack.dto;

import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DescuentoDTO {

    @EqualsAndHashCode.Include
    @Null(message = "No envíes idDescuento al crear", groups = OnCreate.class)
    private Integer idDescuento;

    @NotBlank(message = "El concepto es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 100, message = "El concepto admite máximo 100 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String concepto;

    @Size(max = 200, message = "La descripción admite máximo 200 caracteres", groups = {OnCreate.class, OnUpdate.class})
    private String descripcion;

    @NotNull(message = "El importe es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "0.00", message = "El importe no puede ser negativo", groups = {OnCreate.class, OnUpdate.class})
    @Digits(integer = 6, fraction = 2, message = "Importe con formato inválido (máx 6 enteros y 2 decimales)", groups = {OnCreate.class, OnUpdate.class})
    private Double importe;
}
