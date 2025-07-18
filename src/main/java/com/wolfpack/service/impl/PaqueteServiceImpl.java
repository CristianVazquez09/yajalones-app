package com.wolfpack.service.impl;

import com.wolfpack.model.Paquete;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.repo.IPaqueteRepo;
import com.wolfpack.service.IPaqueteService;
import com.wolfpack.service.IViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaqueteServiceImpl extends CRUDImpl<Paquete, Integer> implements IPaqueteService {
    
    private final IPaqueteRepo repo;
    private final IViajeService viajeService;
    @Override
    protected IGenericRepo<Paquete, Integer> getRepo() {
        return repo;
    }

    @Override
    public Paquete guardarPaquete(Paquete paquete) {
        paquete.setFolio(UUID.randomUUID());

        Paquete paqueteGuardado = repo.save(paquete);
        viajeService.agregarPaquetes(paquete);

        return paqueteGuardado;
    }
}
