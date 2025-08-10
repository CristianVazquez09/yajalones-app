package com.wolfpack.service;

import com.wolfpack.model.Paquete;
import com.wolfpack.model.Pasajero;
import com.wolfpack.model.Viaje;

public interface IViajeService extends ICRUD<Viaje, Integer> {


    Viaje guardarViaje(Viaje viaje);

    void agregarPasajero(Pasajero pasajero);
    void agregarPaquetes(Paquete paquete);

    void actualizarCostosViaje(Integer idViaje);
}
