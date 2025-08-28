package com.wolfpack.service;

import com.wolfpack.model.MovimientoCaja;
import com.wolfpack.model.Paquete;
import com.wolfpack.model.Pasajero;
import com.wolfpack.model.CorteCaja;
import com.wolfpack.model.enums.Terminal;
import com.wolfpack.model.enums.TipoMovimiento;

import java.util.List;

public interface ICajaService /*extends ICRUD<CorteCaja, Long>*/ {
    void registrarPorPasajero(Pasajero p);
    void registrarPorPaquete(Paquete q);
    void cobrarPendiente(Long idMovimientoPendiente, Integer usuarioCobrador, String folioRecibo);

    CorteCaja abrirCorteSiNoExiste(Terminal terminal, String turnoLabel);
    CorteCaja cerrarCorte(Terminal terminal, String turnoLabel);

    // ICajaService.java
    List<MovimientoCaja> pendientes(Terminal terminal);

}
