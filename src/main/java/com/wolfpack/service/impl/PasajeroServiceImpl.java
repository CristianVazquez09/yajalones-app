package com.wolfpack.service.impl;

import com.wolfpack.model.Pasajero;
import com.wolfpack.repo.IPasajeroRepo;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.service.IPasajeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasajeroServiceImpl extends CRUDImpl<Pasajero, Integer> implements IPasajeroService {
    
    private final IPasajeroRepo repo;
    @Override
    protected IGenericRepo<Pasajero, Integer> getRepo() {
        return repo;
    }
}
