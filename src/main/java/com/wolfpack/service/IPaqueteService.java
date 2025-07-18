package com.wolfpack.service;

import com.wolfpack.model.Paquete;

public interface IPaqueteService extends ICRUD<Paquete, Integer> {
    Paquete guardarPaquete(Paquete paquete);
}
