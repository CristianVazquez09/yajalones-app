package com.wolfpack.service.impl;

import com.wolfpack.model.Paquete;
import com.wolfpack.model.Pasajero;
import com.wolfpack.model.Viaje;
import com.wolfpack.model.enums.TipoPago;
import com.wolfpack.model.enums.TipoPasajero;
import com.wolfpack.repo.IViajeRepo;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.service.IViajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViajeServiceImpl extends CRUDImpl<Viaje, Integer> implements IViajeService {
    
    private final IViajeRepo repo;

    private final double COMISION = 340;
    @Override
    protected IGenericRepo<Viaje, Integer> getRepo() {
        return repo;
    }

    @Override
    public Viaje guardarViaje(Viaje viaje) {

        viaje.setComision(COMISION);
        double totalPasajeros = viaje.getTotalPasajeros();

        double totalPaquetes = viaje.getTotalPaqueteria();

        double ingresoTotal = totalPasajeros + totalPaquetes;

        viaje.setTotalViaje(ingresoTotal);

        return repo.save(viaje);
    }

    @Override
    public void agregarPasajero(Pasajero pasajero) {

        Viaje viaje = repo.getReferenceById(pasajero.getViaje().getIdViaje());


        List<Pasajero> pasajeros= viaje.getPasajeros();
        if(viaje.getPasajeros() == null){
            pasajeros = new ArrayList<>();
        }


        pasajeros.add(pasajero);

        double totalPasajeros = sumarImportes(pasajeros);
        double totalPagoSCLC = sumarPorTipoPago(pasajeros, TipoPago.PAGAR_SCLC);
        double totalPagoYajalon = sumarPorTipoPago(pasajeros, TipoPago.PAGAR_YAJALON);

        viaje.setTotalPasajeros(totalPasajeros);
        viaje.setTotalPagadoSclc(totalPagoSCLC);
        viaje.setTotalPagadoYajalon(totalPagoYajalon);
        viaje.setPasajeros(pasajeros);

        guardarViaje(viaje);

    }

    @Override
    public void agregarPaquetes(Paquete paquete) {
        Viaje viaje = repo.getReferenceById(paquete.getViaje().getIdViaje());


        List<Paquete> paquetes= viaje.getPaquetes();
        if(viaje.getPaquetes() == null){
            paquetes = new ArrayList<>();
        }

        paquetes.add(paquete);

        double totalPaquetes = sumarImportes(paquetes);
        double totalPorCobrar = sumarPaquetesPorCobrar(paquetes);

        viaje.setTotalPaqueteria(totalPaquetes);
        viaje.setTotalPorCobrar(totalPorCobrar);
        viaje.setPaquetes(paquetes);

        guardarViaje(viaje);
    }


    private <T> double sumarImportes(List<T> items){
        return items.stream()
                .mapToDouble(item -> {
                    try {
                        Method m = item.getClass().getMethod("getImporte");
                        Object val = m.invoke(item);
                        return val == null ? 0.0 : ((Number) val).doubleValue();

                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sum();
    }

    private double sumarPorTipoPago(List<Pasajero> pasajeros, TipoPago tipoPago){
        return pasajeros.stream()
                .filter(p -> p.getTipoPago() == tipoPago)
                .mapToDouble(Pasajero::getImporte)
                .sum();
    }


    private double sumarPaquetesPorCobrar(List<Paquete> paquetes){
        return paquetes.stream()
                .filter(Paquete::isPorCobrar)
                .mapToDouble(Paquete::getImporte)
                .sum();
    }


}
