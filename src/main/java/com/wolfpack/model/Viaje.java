package com.wolfpack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idViaje;

    @Column(nullable = false, length = 30)
    private String origen;

    @Column(nullable = false, length = 30)
    private String destino;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private double totalPasajeros;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private double totalPaqueteria;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private double comision;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private double totalPorCobrar;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private double totalPagadoYajalon;

    @Column(nullable = false, columnDefinition = "decimal(6,2)")
    private double totalPagadoSclc;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "id_descuento",unique = true, nullable = false)
    private Descuento descuento;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "id_unidad",unique = true, nullable = false)
    private Unidad unidad;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Pasajero> pasajeros;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Paquete> paquetes;


}
