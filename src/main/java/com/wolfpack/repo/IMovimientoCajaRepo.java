package com.wolfpack.repo;

import com.wolfpack.model.MovimientoCaja;
import com.wolfpack.model.enums.Terminal;
import com.wolfpack.model.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IMovimientoCajaRepo extends IGenericRepo<MovimientoCaja, Long> {

    List<MovimientoCaja> findByTerminalAndCreadoEnBetween(Terminal terminal, LocalDateTime from, LocalDateTime to);

    @Query("""
        select coalesce(sum(m.importe),0)
        from MovimientoCaja m
        where m.terminal = :terminal and m.tipo = :tipo and m.creadoEn between :from and :to
    """)
    java.math.BigDecimal sumImporteByTipoAndTerminalAndRange(Terminal terminal, TipoMovimiento tipo,
                                                             LocalDateTime from, LocalDateTime to);

    @Query("""
        select coalesce(sum(m.importe),0)
        from MovimientoCaja m
        where m.terminal = :terminal and m.tipo = com.wolfpack.model.enums.TipoMovimiento.PENDIENTE
          and m.saldado = false
          and m.creadoEn < :apertura
    """)
    java.math.BigDecimal sumPendientesAbiertosAntesDe(Terminal terminal, LocalDateTime apertura);

    // IMovimientoCajaRepo.java
    List<MovimientoCaja> findByTerminalAndTipoAndSaldadoFalse(Terminal terminal, TipoMovimiento tipo);

}
