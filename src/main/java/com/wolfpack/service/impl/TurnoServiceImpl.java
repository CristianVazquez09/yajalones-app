package com.wolfpack.service.impl;

import com.wolfpack.model.Turno;
import com.wolfpack.repo.ITurnoRepo;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.service.ITurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl extends CRUDImpl<Turno, Integer> implements ITurnoService {
    
    private final ITurnoRepo repo;
    @Override
    protected IGenericRepo<Turno, Integer> getRepo() {
        return repo;
    }
}
