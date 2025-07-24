package com.wolfpack.model;

import com.wolfpack.model.enums.TipoPago;
import com.wolfpack.model.enums.TipoPasajero;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pasajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idPasajero;

    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 50)
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPasajero tipo;

    @Column(nullable = false)
    private int asiento;

    @Column(nullable = false, length = 9)
    private String folio;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private double importe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPago tipoPago;

    @ManyToOne
    @JoinColumn(name = "id_viaje", nullable = false, foreignKey = @ForeignKey(name = "FK_PASAJERO_VIAJE"))
    private Viaje viaje;


}
