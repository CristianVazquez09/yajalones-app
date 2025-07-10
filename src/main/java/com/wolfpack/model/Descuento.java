package com.wolfpack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@Entity
public class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idDescuento;

    @Column(nullable = false, length = 100)
    private String concepto;

    @Column(length = 200)
    private String descripcion;

    @Column(nullable = false, columnDefinition = "decimal(8,2)")
    private double importe;
}
