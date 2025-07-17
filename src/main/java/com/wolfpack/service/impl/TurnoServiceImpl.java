package com.wolfpack.service.impl;

import com.wolfpack.model.Chofer;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.repo.IChoferRepo;
import com.wolfpack.service.IChoferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChoferServiceImpl extends CRUDImpl<Chofer, Integer> implements IChoferService {
    
    private final IChoferRepo repo;
    @Override
    protected IGenericRepo<Chofer, Integer> getRepo() {
        return repo;
    }
}
