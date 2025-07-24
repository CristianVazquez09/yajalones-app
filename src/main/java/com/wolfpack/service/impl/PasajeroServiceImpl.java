package com.wolfpack.service.impl;

import com.wolfpack.model.Pasajero;
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
    public Pasajero guardarPasajero(Pasajero pasajero) {
        // Calcular el importe
        double importe = pasajero.getTipo().getTarifaBase();
        //Asignar el importe al pasajero
        pasajero.setImporte(importe);
        pasajero.setFolio(GeneradorFolio.generarFolio());

        // Guardar el pasajero en bd
        Pasajero pasajeroGuardado = repo.save(pasajero);

        // actualizar el viaje
        viajeService.agregarPasajero(pasajero);


        return pasajeroGuardado;
    }

}
