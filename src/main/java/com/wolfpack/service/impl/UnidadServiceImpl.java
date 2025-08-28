package com.wolfpack.service.impl;

import com.wolfpack.model.Unidad;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.repo.IUnidadRepo;
import com.wolfpack.service.ICRUD;
import com.wolfpack.service.IUnidadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnidadServiceImpl extends CRUDImpl<Unidad, Integer> implements IUnidadService {

    private final IUnidadRepo repo;
    @Override
    protected IGenericRepo<Unidad, Integer> getRepo() {
        return repo;
    }

    @Override
    public void eliminarUnidad(Integer id) {
        Unidad unidad = repo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No existe unidad con id: " + id)
        );

        unidad.setTurno(null);

        repo.save(unidad);
    }
}
