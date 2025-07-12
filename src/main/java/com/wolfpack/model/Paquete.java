package com.wolfpack.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idPaquete;

    @Column(nullable = false, length = 100)
    private String remitente;

    @Column(nullable = false, length = 100)
    private String destinatario;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private double importe;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private UUID folio;

    @Column(nullable = false)
    private boolean posCobrar;

    @Column(nullable = false)
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_viaje", nullable = false, foreignKey = @ForeignKey(name = "FK_PAQUETERIA_VIAJE"))
    private Viaje viaje;
}
