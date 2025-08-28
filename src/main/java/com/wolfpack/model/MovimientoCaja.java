package com.wolfpack.model;

import com.wolfpack.model.audit.Auditable;
import com.wolfpack.model.enums.CategoriaMovimiento;
import com.wolfpack.model.enums.Terminal;
import com.wolfpack.model.enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "movimiento_caja",
        indexes = {
                @Index(name = "idx_mc_terminal_fecha", columnList = "terminal, creado_en"),
                @Index(name = "idx_mc_tipo_terminal", columnList = "tipo, terminal")
        })
public class MovimientoCaja extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Terminal terminal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimiento tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoriaMovimiento categoria;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importe;

    @Column(nullable = false)
    private Integer idViaje;

    @Column(nullable = false)
    private Integer idReferencia;

    private Long idPendienteOrigen;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(nullable = false)
    private boolean saldado;

    private LocalDateTime saldadoEn;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
