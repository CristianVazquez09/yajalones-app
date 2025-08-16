package com.wolfpack.service;

import com.wolfpack.model.Paquete;
import com.wolfpack.model.Paquete;

import java.util.List;

public interface IPaqueteService extends ICRUD<Paquete, Integer> {
    Paquete guardarPaquete(Paquete paquete);
    Paquete actualizarPaquete(Integer id , Paquete paquete) throws Exception;
    void eliminarPaquete(Integer idPaquete) throws Exception;
    Paquete guardarPaquetePendiente(Paquete paquete);

    Paquete confirmarPaquete(Integer idPaquete, Integer idViaje);
    Paquete desconfirmarPaquete(Integer idPaquete, Integer idViaje);
    List<Paquete> obtenerPaquetesPendientes ();
}

