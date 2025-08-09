package com.wolfpack.repo;


import com.wolfpack.model.Paquete;

import java.util.List;

public interface IPaqueteRepo extends IGenericRepo<Paquete, Integer> {

    List<Paquete> findByEstadoIsFalse ();


}
