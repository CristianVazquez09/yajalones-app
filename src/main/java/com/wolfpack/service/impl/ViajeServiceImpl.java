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

        List<Pasajero> pasajeros = Optional.ofNullable(viaje.getPasajeros())
                .orElseGet(Collections::emptyList);
        List<Paquete> paquetes = Optional.ofNullable(viaje.getPaquetes())
                .orElseGet(Collections::emptyList);

        double totalPasajeros = sumarImportes(pasajeros);

        double totalPaquetes = sumarImportes(paquetes);

        double totalPagoSCLC = sumarPorTipoPago(pasajeros, TipoPago.PAGAR_SCLC);
        double totalPagoYajalon = sumarPorTipoPago(pasajeros, TipoPago.PAGAR_YAJALON);
        double totalPorCobrar = sumarPaquetesPorCobrar(paquetes);

        viaje.setTotalPasajeros(totalPasajeros);
        viaje.setTotalPaqueteria(totalPaquetes);
        viaje.setTotalPagadoSclc(totalPagoSCLC);
        viaje.setTotalPagadoYajalon(totalPagoYajalon);
        viaje.setTotalPorCobrar(totalPorCobrar);
        viaje.setComision(COMISION);

        double ingresoTotal = totalPasajeros + totalPaquetes;
        if (ingresoTotal <= COMISION) {
            throw new IllegalArgumentException(
                    String.format(
                            "La suma de ingresos (%.2f + %.2f = %.2f) debe ser mayor que la comisión (%.2f)",
                            totalPasajeros, totalPaquetes, ingresoTotal, COMISION
                    )
            );
        }
        viaje.setTotalViaje(ingresoTotal);

        return repo.save(viaje);
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
                .filter(Paquete::isPosCobrar)
                .mapToDouble(Paquete::getImporte)
                .sum();
    }


}
