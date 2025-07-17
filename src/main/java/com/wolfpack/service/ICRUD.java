package com.wolfpack.service;

import java.util.List;

public interface ICRUD<T, ID> {

    T guardar(T t) throws Exception;
    T actualizar(ID id, T t) throws Exception;
    List<T> buscarTodos() throws Exception;
    T buscarPorId(ID id) throws Exception;
    void eliminar(ID id) throws Exception;
}
