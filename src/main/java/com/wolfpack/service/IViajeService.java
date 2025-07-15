package com.wolfpack.service;

import com.wolfpack.model.Viaje;

public interface IViajeService extends ICRUD<Viaje, Integer> {


    Viaje guardarViaje(Viaje viaje);
}
