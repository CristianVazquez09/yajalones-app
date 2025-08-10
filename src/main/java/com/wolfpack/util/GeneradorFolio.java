package com.wolfpack.util;

import java.util.UUID;

public class GeneradorFolio {

        public static String generarFolio() {
            // UUID sin guiones, en mayúsculas
            String uuid = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .toUpperCase();
            // Tomamos los primeros 8 caracteres
            return uuid.substring(0, 8);
        }
}
