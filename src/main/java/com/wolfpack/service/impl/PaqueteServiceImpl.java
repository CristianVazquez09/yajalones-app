package com.wolfpack.service.impl;

import com.wolfpack.model.Paquete;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.repo.IPaqueteRepo;
import com.wolfpack.service.IPaqueteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaqueteServiceImpl extends CRUDImpl<Paquete, Integer> implements IPaqueteService {
    
    private final IPaqueteRepo repo;
    @Override
    protected IGenericRepo<Paquete, Integer> getRepo() {
        return repo;
    }
}
