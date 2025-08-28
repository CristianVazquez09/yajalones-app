package com.wolfpack.model;

import com.wolfpack.model.audit.Auditable;
import com.wolfpack.model.enums.EstadoCorte;
import com.wolfpack.model.enums.Terminal;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "corte_caja", uniqueConstraints = {
        @UniqueConstraint(name = "uq_corte_abierto", columnNames = {"terminal", "estado"})
})
public class CorteCaja extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCorte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Terminal terminal;

    @Column(nullable = false, length = 50)
    private String turnoLabel;

    @Column(nullable = false)
    private LocalDateTime apertura;

    private LocalDateTime cierre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCorte estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal ingresos;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pendientesCreados;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pendientesCobrados;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoInicialPend;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoFinalPend;
}
