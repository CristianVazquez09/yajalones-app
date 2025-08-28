package com.wolfpack.controller;

import com.wolfpack.model.CorteCaja;
import com.wolfpack.model.MovimientoCaja;
import com.wolfpack.model.enums.Terminal;
import com.wolfpack.service.ICajaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caja")
@RequiredArgsConstructor
public class CajaController {

    private final ICajaService caja;

    @PostMapping("/abrir/{terminal}")
    public ResponseEntity<CorteCaja> abrir(@PathVariable Terminal terminal, @RequestBody TurnoReq req) {
        return ResponseEntity.ok(caja.abrirCorteSiNoExiste(terminal, req.getTurnoLabel()));
    }

    @PostMapping("/cerrar/{terminal}")
    public ResponseEntity<CorteCaja> cerrar(@PathVariable Terminal terminal, @RequestBody TurnoReq req) {
        return ResponseEntity.ok(caja.cerrarCorte(terminal, req.getTurnoLabel()));
    }

    @PostMapping("/cobrar/{idPendiente}")
    public ResponseEntity<Void> cobrar(@PathVariable Long idPendiente, @RequestBody CobroReq req) {
        caja.cobrarPendiente(idPendiente, req.getFolioRecibo());
        return ResponseEntity.ok().build();
    }

    // CajaController.java
    @GetMapping("/pendientes/{terminal}")
    public ResponseEntity<List<MovimientoCaja>> pendientes(@PathVariable Terminal terminal) {
        return ResponseEntity.ok(caja.pendientes(terminal));
    }


    @Data
    public static class TurnoReq { private String turnoLabel; }
    @Data
    public static class CobroReq { private Integer usuarioId; private String folioRecibo; }
}
