package com.wolfpack.service;

import com.wolfpack.model.Paquete;
import com.wolfpack.model.Paquete;

public interface IPaqueteService extends ICRUD<Paquete, Integer> {
    Paquete guardarPaquete(Paquete paquete);
    Paquete actualizarPaquete(Integer id , Paquete paquete) throws Exception;
    void eliminarPaquete(Integer idPaquete) throws Exception;
}
