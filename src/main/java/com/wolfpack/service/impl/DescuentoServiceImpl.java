package com.wolfpack.service.impl;

import com.wolfpack.model.Descuento;
import com.wolfpack.repo.IDescuentoRepo;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.service.IDescuentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DescuentoServiceImpl extends CRUDImpl<Descuento, Integer> implements IDescuentoService {
    
    private final IDescuentoRepo repo;
    @Override
    protected IGenericRepo<Descuento, Integer> getRepo() {
        return repo;
    }
}
