package com.wolfpack.service.impl;

import com.wolfpack.model.Viaje;
import com.wolfpack.repo.IViajeRepo;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.service.IViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViajeServiceImpl extends CRUDImpl<Viaje, Integer> implements IViajeService {
    
    private final IViajeRepo repo;
    @Override
    protected IGenericRepo<Viaje, Integer> getRepo() {
        return repo;
    }
}
