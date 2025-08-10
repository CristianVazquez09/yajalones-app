package com.wolfpack.model.enums;

import lombok.Getter;

@Getter
public enum TipoPasajero {
    ADULTO(320.0, 350.0),
    NIÑO(290.0, 290),
    INCENT_INAPAM(310.0, 310);

    private final double tarifaYajalonSanCristobal;
    private final double tarifaYajalonTuxtla;

    TipoPasajero(double tarifaYajalonSanCristobal, double tarifaYajalonTuxtla) {
        this.tarifaYajalonSanCristobal  = tarifaYajalonSanCristobal;
        this.tarifaYajalonTuxtla = tarifaYajalonTuxtla;
    }
}


