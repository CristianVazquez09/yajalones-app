package com.wolfpack.dto;

import com.wolfpack.util.OnCreate;
import com.wolfpack.util.OnUpdate;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoDTO {

    @Null(message = "No especifiques idTurno al crear", groups = OnCreate.class)
    @Null(message = "El id dek Turno debe ir la URL", groups = OnUpdate.class)
    private Integer idTurno;

    @NotNull(message = "El horario es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private Time horario;

    @NotNull(message = "El estado activo es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    private Boolean activo;

}
