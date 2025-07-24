package com.wolfpack.service.impl;

import com.wolfpack.exception.ModelNotFoundException;
import com.wolfpack.model.Pasajero;
import com.wolfpack.model.Viaje;
import com.wolfpack.model.enums.TipoPasajero;
import com.wolfpack.repo.IPasajeroRepo;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.repo.IViajeRepo;
import com.wolfpack.service.IPasajeroService;
import com.wolfpack.service.IViajeService;
import com.wolfpack.util.GeneradorFolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasajeroServiceImpl extends CRUDImpl<Pasajero, Integer> implements IPasajeroService {
    
    private final IPasajeroRepo repo;
    private final IViajeService viajeService;
    @Override
    protected IGenericRepo<Pasajero, Integer> getRepo() {
        return repo;
    }

    @Override
    public Pasajero guardarPasajero(Pasajero pasajero) throws Exception {
        Viaje viaje = viajeService.buscarPorId(pasajero.getViaje().getIdViaje());
        if (viaje == null) {
            throw new IllegalArgumentException("El viaje no puede ser nulo");
        }

        // 1. Calcula importe según origen/destino
        double importe = calcularImportePorRuta(
                pasajero.getTipo(),
                viaje.getOrigen(),
                viaje.getDestino()
        );

        // 2. Asigna importe y folio
        pasajero.setImporte(importe);
        pasajero.setFolio(GeneradorFolio.generarFolio());

        // 3. Persiste pasajero
        Pasajero guardado = repo.save(pasajero);

        // 4. Asocia al viaje y actualiza totales
        viajeService.agregarPasajero(guardado);

        return guardado;
    }

    @Override
    public Pasajero actualizarPasajero(Integer id, Pasajero pasajero) throws Exception {
        Pasajero pasajeroEncontrado = repo.findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + id));

        pasajero.setFolio(pasajeroEncontrado.getFolio());
        pasajero.setImporte(pasajeroEncontrado.getImporte());

        return repo.save(pasajero);
    }

    @Override
    public void eliminarPasajero(Integer idPasajero) throws Exception {
        Pasajero pasajero =  repo.findById(idPasajero).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + idPasajero));
        Integer idViaje = pasajero.getViaje().getIdViaje();
        repo.deleteById(idPasajero);
        viajeService.actualizarCostosViaje(idViaje);


    }


    private double calcularImportePorRuta(
            TipoPasajero tipo,
            String origen,
            String destino
    ) {
        String o = origen.trim().toLowerCase();
        String d = destino.trim().toLowerCase();

        boolean esSC_YA = (o.equals("san cristobal de las casas") && d.equals("yajalon"))
                || (o.equals("yajalon")                && d.equals("san cristobal de las casas"));

        boolean esTU_YA = (o.equals("tuxtla gutierrez") && d.equals("yajalon"))
                || (o.equals("yajalon") && d.equals("tuxtla gutierrez"));

        if (esSC_YA) {
            return tipo.getTarifaYajalonSanCristobal();
        }
        else if (esTU_YA) {
            return tipo.getTarifaYajalonTuxtla();
        }
        throw new IllegalArgumentException(
                String.format("Ruta no soportada: %s → %s", origen, destino)
        );
    }


}
