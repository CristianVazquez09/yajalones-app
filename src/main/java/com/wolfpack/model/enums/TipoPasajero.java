package com.wolfpack.model.enums;

import lombok.Getter;

@Getter
public enum TipoPasajero {
    ADULTO(350.0),
    NIÑO(290.0),
    INCENT_INAPAM(310.0);

    private final double tarifaBase;
    TipoPasajero(double tarifaBase){
        this.tarifaBase = tarifaBase;
    }
}

