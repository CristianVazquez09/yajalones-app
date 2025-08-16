package com.wolfpack.service.impl;

import com.wolfpack.exception.ModelNotFoundException;
import com.wolfpack.model.Paquete;
import com.wolfpack.model.Paquete;
import com.wolfpack.model.Paquete;
import com.wolfpack.model.Viaje;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.repo.IPaqueteRepo;
import com.wolfpack.repo.IViajeRepo;
import com.wolfpack.service.IPaqueteService;
import com.wolfpack.service.IViajeService;
import com.wolfpack.util.GeneradorFolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaqueteServiceImpl extends CRUDImpl<Paquete, Integer> implements IPaqueteService {
    
    private final IPaqueteRepo repo;
    private final IViajeService viajeService;
    private final IViajeRepo viajeRepo;
    @Override
    protected IGenericRepo<Paquete, Integer> getRepo() {
        return repo;
    }

    @Override
    public Paquete guardarPaquete(Paquete paquete) {
        paquete.setFolio(GeneradorFolio.generarFolio());
        paquete.setEstado(true);
        Paquete paqueteGuardado = repo.save(paquete);
        viajeService.agregarPaquetes(paquete);

        return paqueteGuardado;
    }

    @Override
    public Paquete actualizarPaquete(Integer id, Paquete paquete) throws Exception {
        Paquete paqueteEncontrado = repo.findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + id));

        paquete.setFolio(paqueteEncontrado.getFolio());
        Paquete paqueteActualizado = repo.save(paquete);
        viajeService.actualizarCostosViaje(paqueteActualizado.getViaje().getIdViaje());

        return paqueteActualizado;
    }

    @Override
    public void eliminarPaquete(Integer idPaquete) throws Exception {
        Paquete paquete =  repo.findById(idPaquete).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + idPaquete));
        Integer idViaje = paquete.getViaje().getIdViaje();
        repo.deleteById(idPaquete);
        viajeService.actualizarCostosViaje(idViaje);


    }

    @Override
    public Paquete guardarPaquetePendiente(Paquete paquete) {
        paquete.setFolio(GeneradorFolio.generarFolio());
        paquete.setEstado(false);
        return repo.save(paquete);
    }

    @Override
    public Paquete confirmarPaquete(Integer idPaquete, Integer idViaje) {
        Paquete paqueteEncontrado = repo.findById(idPaquete).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + idPaquete));
        Viaje viajeEncontrado = viajeRepo.findById(idViaje).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + idViaje));

        paqueteEncontrado.setEstado(true);
        paqueteEncontrado.setViaje(viajeEncontrado);

        Paquete paqueteGuardado = repo.save(paqueteEncontrado);
        viajeService.agregarPaquetes(paqueteGuardado);


        return paqueteGuardado ;
    }

    @Override
    public Paquete desconfirmarPaquete(Integer idPaquete, Integer idViaje) {
        Paquete paqueteEncontrado = repo.findById(idPaquete).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + idPaquete));
        Viaje viajeEncontrado = viajeRepo.findById(idViaje).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: " + idViaje));

        paqueteEncontrado.setEstado(false);

        paqueteEncontrado.setViaje(viajeEncontrado);

        viajeService.darBajaPaquete(paqueteEncontrado, idViaje);
        return paqueteEncontrado;
    }

    @Override
    public List<Paquete> obtenerPaquetesPendientes() {
        return repo.findByEstadoIsFalse();
    }
}
