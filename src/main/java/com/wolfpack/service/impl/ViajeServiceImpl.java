package com.wolfpack.service.impl;

import com.wolfpack.model.Paquete;
import com.wolfpack.model.Pasajero;
import com.wolfpack.model.Viaje;
import com.wolfpack.model.enums.TipoPago;
import com.wolfpack.repo.IPaqueteRepo;
import com.wolfpack.repo.IViajeRepo;
import com.wolfpack.repo.IGenericRepo;
import com.wolfpack.service.IViajeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViajeServiceImpl extends CRUDImpl<Viaje, Integer> implements IViajeService {
    
    private final IViajeRepo repo;
    private final IPaqueteRepo paqueteRepo;

    private final double COMISION = 340;
    @Override
    protected IGenericRepo<Viaje, Integer> getRepo() {
        return repo;
    }

    /// CORREGIR --------------------------------------------------------------------------------------
    @Override
    public Viaje guardarViaje(Viaje viaje) {
        viaje.setComision(COMISION);

        return repo.save(viaje);
    }

    @Override
    public void agregarPasajero(Pasajero pasajero) {
        Integer idViaje = pasajero.getViaje().getIdViaje();
        actualizarCostosViaje(idViaje);
    }

    @Override
    public void agregarPaquetes(Paquete paquete) {
        Integer idViaje = paquete.getViaje().getIdViaje();
        actualizarCostosViaje(idViaje);
    }

    @Override
    public void actualizarCostosViaje(Integer idViaje) {
        Viaje v = repo.findById(idViaje)
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado: " + idViaje));

        String origen = norm(v.getOrigen());
        String destino = norm(v.getDestino());

        double ingresoTotal = 0;
        // Null-safety
        List<Pasajero> pasajeros = Optional.ofNullable(v.getPasajeros())
                .orElseGet(List::of);
        List<Paquete>  paquetes   = Optional.ofNullable(v.getPaquetes())
                .orElseGet(List::of);

        double totalPasajeros = sumarImportes(pasajeros);
        double totalPagoSCLC = sumarPorTipoPago(pasajeros, TipoPago.SCLC);
        double totalPagadoTuxtla = 0;
        double totalPagoYajalon = 0;


        switch (origen + "->" + destino) {
            case "TUXTLA->YAJALON" -> {
                totalPagoYajalon = sumarPorTipoPago(pasajeros, TipoPago.DESTINO);
                totalPagadoTuxtla = sumarPorTipoPago(pasajeros, TipoPago.PAGADO);
                ingresoTotal = totalPasajeros - (totalPagoYajalon + totalPagoSCLC);

            }
            case "YAJALON->TUXTLA" -> {
                totalPagoYajalon = sumarPorTipoPago(pasajeros, TipoPago.PAGADO);
                totalPagadoTuxtla = sumarPorTipoPago(pasajeros, TipoPago.DESTINO);
                ingresoTotal = totalPasajeros - (totalPagadoTuxtla + totalPagoSCLC);
            }

        }

        double totalPorCobrar   = sumarPaquetesPorCobrar(paquetes);
        double totalPaquetes    = sumarImportes(paquetes);
        double totalPaqueteria = totalPaquetes - totalPorCobrar;
        ingresoTotal = (ingresoTotal + totalPaqueteria) - COMISION;

        v.setTotalViaje(ingresoTotal);
        v.setTotalPasajeros(totalPasajeros);
        v.setTotalPorCobrar(totalPorCobrar);
        v.setTotalPaqueteria(totalPaquetes);
        v.setTotalPagadoSclc(totalPagoSCLC);
        v.setTotalPagadoTuxtla(totalPagadoTuxtla);
        v.setTotalPagadoYajalon(totalPagoYajalon);

        repo.save(v);
    }

    @Override
    public void darBajaPaquete(Paquete paquete, Integer idViaje) {
        Viaje viaje = repo.findById(idViaje).orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado: " + idViaje));

        paquete.setViaje(null);
        paqueteRepo.save(paquete);
        repo.save(viaje);
        actualizarCostosViaje(idViaje);

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

    private static String norm(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // quita acentos
                .trim()
                .toUpperCase(Locale.ROOT);
    }


}
