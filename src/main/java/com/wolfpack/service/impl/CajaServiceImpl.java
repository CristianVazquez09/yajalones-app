package com.wolfpack.service.impl;

import com.wolfpack.model.*;
import com.wolfpack.model.enums.*;
import com.wolfpack.repo.ICorteCajaRepo;
import com.wolfpack.repo.IMovimientoCajaRepo;
import com.wolfpack.repo.IViajeRepo;
import com.wolfpack.service.ICajaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CajaServiceImpl implements ICajaService {

    private final IMovimientoCajaRepo movRepo;
    private final ICorteCajaRepo corteRepo;
    private final IViajeRepo viajeRepo;

    @Transactional
    @Override
    public void registrarPorPasajero(Pasajero p) {
        Viaje v = viajeRepo.findById(p.getViaje().getIdViaje())
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado"));

        Terminal origen = toTerminal(v.getOrigen());
        Terminal destino = toTerminal(v.getDestino());

        switch (p.getTipoPago()) {
            case PAGADO -> crearIngreso(origen, CategoriaMovimiento.PASAJERO, p.getImporte(), v.getIdViaje(), p.getIdPasajero(), "Pasajero pagado en origen");
            case DESTINO, SCLC -> crearPendiente(destino, CategoriaMovimiento.PASAJERO, p.getImporte(), v.getIdViaje(), p.getIdPasajero(), "Pasajero por cobrar en destino");
            default -> throw new IllegalArgumentException("TipoPago no soportado: " + p.getTipoPago());
        }
    }

    @Transactional
    @Override
    public void registrarPorPaquete(Paquete q) {
        Viaje v = viajeRepo.findById(q.getViaje().getIdViaje())
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado"));

        Terminal origen = toTerminal(v.getOrigen());
        Terminal destino = toTerminal(v.getDestino());

        if (q.isPorCobrar()) {
            crearPendiente(destino, CategoriaMovimiento.PAQUETE, q.getImporte(), v.getIdViaje(), q.getIdPaquete(), "Paquete por cobrar en destino");
        } else {
            crearIngreso(origen, CategoriaMovimiento.PAQUETE, q.getImporte(), v.getIdViaje(), q.getIdPaquete(), "Paquete pagado en origen");
        }
    }

    @Transactional
    @Override
    // Cobrar pendiente (solo recibo folio opcional)
    public void cobrarPendiente(Long idMovimientoPendiente, String folioRecibo) {
        var pend = movRepo.findById(idMovimientoPendiente)
                .orElseThrow(() -> new EntityNotFoundException("Pendiente no encontrado: " + idMovimientoPendiente));
        if (pend.getTipo() != TipoMovimiento.PENDIENTE || pend.isSaldado()) {
            throw new IllegalStateException("No es un pendiente activo");
        }

        pend.setSaldado(true);
        pend.setSaldadoEn(java.time.LocalDateTime.now());
        movRepo.save(pend); // @LastModifiedBy se rellena aquí

        var cobro = MovimientoCaja.builder()
                .terminal(pend.getTerminal())
                .tipo(TipoMovimiento.COBRO_PENDIENTE)
                .categoria(pend.getCategoria())
                .importe(pend.getImporte())
                .idViaje(pend.getIdViaje())
                .idReferencia(pend.getIdReferencia())
                .idPendienteOrigen(pend.getIdMovimiento())
                .creadoEn(java.time.LocalDateTime.now())
                .saldado(false)
                .descripcion("Cobro pendiente")
                .build();
        movRepo.save(cobro); // @CreatedBy = usuario que cobró
    }


    @Transactional
    @Override
    public CorteCaja abrirCorteSiNoExiste(Terminal terminal, String turnoLabel) {
        CorteCaja corte = corteRepo.findByTerminalAndEstado(terminal, EstadoCorte.ABIERTO)
                .orElseGet(() -> corteRepo.save(CorteCaja.builder()
                        .terminal(terminal)
                        .turnoLabel(turnoLabel)
                        .apertura(java.time.LocalDateTime.now())
                        .estado(EstadoCorte.ABIERTO)
                        .ingresos(java.math.BigDecimal.ZERO)
                        .pendientesCreados(java.math.BigDecimal.ZERO)
                        .pendientesCobrados(java.math.BigDecimal.ZERO)
                        .saldoInicialPend(java.math.BigDecimal.ZERO)
                        .saldoFinalPend(java.math.BigDecimal.ZERO)
                        .build()));

        // @CreatedBy registra quién abrió. Si además quieres el campo explícito:
        //corte.setAbiertoPor(corte.getCreatedBy());
        return corteRepo.save(corte);
    }

    @Transactional
    @Override
    public CorteCaja cerrarCorte(Terminal terminal, String turnoLabel) {
        CorteCaja abierto = corteRepo.findByTerminalAndEstado(terminal, EstadoCorte.ABIERTO)
                .orElseThrow(() -> new IllegalStateException("No hay corte ABIERTO para " + terminal));

        LocalDateTime desde = abierto.getApertura();
        LocalDateTime hasta = LocalDateTime.now();

        BigDecimal saldoInicial = movRepo.sumPendientesAbiertosAntesDe(terminal, desde);
        BigDecimal ingresos = movRepo.sumImporteByTipoAndTerminalAndRange(terminal, TipoMovimiento.INGRESO, desde, hasta);
        BigDecimal pendCreados = movRepo.sumImporteByTipoAndTerminalAndRange(terminal, TipoMovimiento.PENDIENTE, desde, hasta);
        BigDecimal pendCobrados = movRepo.sumImporteByTipoAndTerminalAndRange(terminal, TipoMovimiento.COBRO_PENDIENTE, desde, hasta);

        BigDecimal saldoFinal = saldoInicial.add(pendCreados).subtract(pendCobrados);

        abierto.setCierre(hasta);
        abierto.setEstado(EstadoCorte.CERRADO);
        abierto.setTurnoLabel(turnoLabel);
        abierto.setSaldoInicialPend(saldoInicial);
        abierto.setIngresos(ingresos);
        abierto.setPendientesCreados(pendCreados);
        abierto.setPendientesCobrados(pendCobrados);
        //abierto.setCerradoPor(abierto.getLastModifiedBy()); // el que ejecuta cierre
        abierto.setSaldoFinalPend(saldoFinal);

        return corteRepo.save(abierto);
    }

    @Override
    public List<MovimientoCaja> pendientes(Terminal terminal) {
        return movRepo.findByTerminalAndTipoAndSaldadoFalse(terminal, TipoMovimiento.PENDIENTE);
    }

    private void crearIngreso(Terminal terminal, CategoriaMovimiento cat, double monto, Integer idViaje, Integer idRef, String desc) {
        movRepo.save(MovimientoCaja.builder()
                .terminal(terminal)
                .tipo(TipoMovimiento.INGRESO)
                .categoria(cat)
                .importe(BigDecimal.valueOf(monto))
                .idViaje(idViaje)
                .idReferencia(idRef)
                .creadoEn(LocalDateTime.now())
                .saldado(true)
                .descripcion(desc)
                .build());
    }

    private void crearPendiente(Terminal terminal, CategoriaMovimiento cat, double monto, Integer idViaje, Integer idRef, String desc) {
        movRepo.save(MovimientoCaja.builder()
                .terminal(terminal)
                .tipo(TipoMovimiento.PENDIENTE)
                .categoria(cat)
                .importe(BigDecimal.valueOf(monto))
                .idViaje(idViaje)
                .idReferencia(idRef)
                .creadoEn(LocalDateTime.now())
                .saldado(false)
                .descripcion(desc)
                .build());
    }

    private Terminal toTerminal(String nombre) {
        String s = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toUpperCase();
        return switch (s) {
            case "TUXTLA" -> Terminal.TUXTLA;
            case "YAJALON" -> Terminal.YAJALON;
            default -> throw new IllegalArgumentException("Terminal no soportada: " + nombre);
        };
    }
}
