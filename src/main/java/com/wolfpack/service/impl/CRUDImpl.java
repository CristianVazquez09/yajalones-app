package com.wolfpack.service.impl;

import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.service.ICRUD;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public abstract class CRUDImpl<T, ID> implements ICRUD<T, ID> {

    protected abstract IGenericRepo<T, ID> getRepo();

    @Override
    public T guardar(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public T actualizar(ID id, T t) throws Exception {
        getRepo().findById(id).orElseThrow(() -> new EntityNotFoundException("ID no encontrado: " + id));
        return getRepo().save(t);
    }

    @Override
    public List<T> buscarTodos() throws Exception {
        return getRepo().findAll();
    }

    @Override
    public T buscarPorId(ID id) throws Exception {
        return getRepo().findById(id).orElseThrow(()-> new EntityNotFoundException("ID no encontrado: " + id));
    }

    @Override
    public void eliminar(ID id) throws Exception {
        getRepo().findById(id).orElseThrow(() -> new EntityNotFoundException("ID no encontrado: " + id));
        getRepo().deleteById(id);
    }
}
