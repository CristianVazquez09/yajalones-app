package com.wolfpack.service;

import com.wolfpack.model.Pasajero;

public interface IPasajeroService extends ICRUD<Pasajero, Integer> {
    Pasajero guardarPasajero(Pasajero pasajero) throws Exception;
    Pasajero actualizarPasajero(Integer id , Pasajero pasajero) throws Exception;
    void eliminarPasajero(Integer idPasajero) throws Exception;
}
