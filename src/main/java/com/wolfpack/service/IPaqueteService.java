package com.wolfpack.service;

import com.wolfpack.model.Paquete;
import com.wolfpack.model.Pasajero;

public interface IPaqueteService extends ICRUD<Paquete, Integer> {
    Paquete guardarPaquete(Paquete paquete);
    Paquete actualizarPaquete(Integer id , Paquete paquete) throws Exception;
}
