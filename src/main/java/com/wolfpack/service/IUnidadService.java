package com.wolfpack.service;

import com.wolfpack.model.Unidad;

public interface IUnidadService extends ICRUD<Unidad, Integer> {

    void eliminarUnidad(Integer id);
}
