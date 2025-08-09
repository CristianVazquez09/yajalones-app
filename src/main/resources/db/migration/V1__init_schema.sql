-- === Turno ===
CREATE TABLE IF NOT EXISTS turno (
                                     id_turno   SERIAL PRIMARY KEY,
                                     horario    TIME NOT NULL,
                                     activo     BOOLEAN NOT NULL
);

-- === Unidad ===
CREATE TABLE IF NOT EXISTS unidad (
                                      id_unidad   SERIAL PRIMARY KEY,
                                      nombre      VARCHAR(10)  NOT NULL,
                                      descripcion VARCHAR(200),
                                      activo      BOOLEAN      NOT NULL,
                                      id_turno    INTEGER      NOT NULL,
                                      CONSTRAINT fk_unidad_turno
                                          FOREIGN KEY (id_turno) REFERENCES turno(id_turno)
);

-- === Chofer ===
CREATE TABLE IF NOT EXISTS chofer (
                                      id_chofer SERIAL PRIMARY KEY,
                                      nombre    VARCHAR(50) NOT NULL,
                                      apellido  VARCHAR(50) NOT NULL,
                                      telefono  VARCHAR(11) NOT NULL UNIQUE,
                                      activo    BOOLEAN     NOT NULL,
                                      id_unidad INTEGER,
                                      CONSTRAINT fk_chofer_unidad
                                          FOREIGN KEY (id_unidad) REFERENCES unidad(id_unidad)
);

-- === Descuento ===
CREATE TABLE IF NOT EXISTS descuento (
                                         id_descuento SERIAL PRIMARY KEY,
                                         concepto     VARCHAR(100) NOT NULL,
                                         descripcion  VARCHAR(200),
                                         importe      DECIMAL(8,2) NOT NULL
);

-- === Viaje ===
CREATE TABLE IF NOT EXISTS viaje (
                                     id_viaje             SERIAL PRIMARY KEY,
                                     origen               VARCHAR(30) NOT NULL,
                                     destino              VARCHAR(30) NOT NULL,
                                     fecha_salida         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                     total_pasajeros      DECIMAL(6,2) NOT NULL,
                                     total_paqueteria     DECIMAL(6,2) NOT NULL,
                                     comision             DECIMAL(6,2) NOT NULL,
                                     total_por_cobrar     DECIMAL(6,2) NOT NULL,
                                     total_pagado_yajalon DECIMAL(6,2) NOT NULL,
                                     total_pagado_sclc    DECIMAL(6,2) NOT NULL,
                                     id_descuento         INTEGER UNIQUE,
                                     id_unidad            INTEGER NOT NULL,
                                     total_viaje          DECIMAL(8,2) NOT NULL,
                                     CONSTRAINT fk_viaje_descuento
                                         FOREIGN KEY (id_descuento) REFERENCES descuento(id_descuento),
                                     CONSTRAINT fk_viaje_unidad
                                         FOREIGN KEY (id_unidad) REFERENCES unidad(id_unidad)
);

-- === Pasajero ===
-- Enums como STRING: agrega checks para valores válidos
-- TipoPasajero: ADULTO, NIÑO, INCENT_INAPAM
-- TipoPago: SCLC, DESTINO, PAGADO
CREATE TABLE IF NOT EXISTS pasajero (
                                        id_pasajero SERIAL PRIMARY KEY,
                                        nombre      VARCHAR(50) NOT NULL,
                                        apellido    VARCHAR(50) NOT NULL,
                                        tipo        VARCHAR(255) NOT NULL,
                                        asiento     INTEGER NOT NULL,
                                        folio       VARCHAR(9) NOT NULL,
                                        importe     DECIMAL(6,2) NOT NULL,
                                        tipo_pago   VARCHAR(255) NOT NULL,
                                        id_viaje    INTEGER NOT NULL,
                                        CONSTRAINT fk_pasajero_viaje
                                            FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje),
                                        CONSTRAINT ck_pasajero_tipo
                                            CHECK (tipo IN ('ADULTO','NIÑO','INCENT_INAPAM')),
                                        CONSTRAINT ck_pasajero_tipo_pago
                                            CHECK (tipo_pago IN ('SCLC','DESTINO','PAGADO'))
);

-- === Paquete ===
CREATE TABLE IF NOT EXISTS paquete (
                                       id_paquete  SERIAL PRIMARY KEY,
                                       remitente   VARCHAR(100) NOT NULL,
                                       destinatario VARCHAR(100) NOT NULL,
                                       importe     DECIMAL(6,2) NOT NULL,
                                       contenido   TEXT NOT NULL,
                                       folio       VARCHAR(9) NOT NULL,
                                       por_cobrar  BOOLEAN NOT NULL,
                                       estado      BOOLEAN NOT NULL,
                                       id_viaje    INTEGER,
                                       CONSTRAINT fk_paqueteria_viaje
                                           FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje)
);

-- === Seguridad: Rol / Usuario / N:M ===
CREATE TABLE IF NOT EXISTS rol (
                                   id_rol      SERIAL PRIMARY KEY,
                                   name        VARCHAR(50)  NOT NULL,
                                   description VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario (
                                       id_usuario     SERIAL PRIMARY KEY,
                                       nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
                                       password       VARCHAR(70) NOT NULL UNIQUE
    -- Nota: tener password UNIQUE no es usual; considera quitar la unicidad
);

CREATE TABLE IF NOT EXISTS usuario_rol (
                                           id_usuario INTEGER NOT NULL,
                                           id_rol     INTEGER NOT NULL,
                                           PRIMARY KEY (id_usuario, id_rol),
                                           CONSTRAINT fk_usuario_rol_usuario
                                               FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
                                           CONSTRAINT fk_usuario_rol_rol
                                               FOREIGN KEY (id_rol)     REFERENCES rol(id_rol)       ON DELETE CASCADE
);

-- Índices útiles (opcionales)
CREATE INDEX IF NOT EXISTS idx_viaje_unidad        ON viaje(id_unidad);
CREATE INDEX IF NOT EXISTS idx_pasajero_viaje      ON pasajero(id_viaje);
CREATE INDEX IF NOT EXISTS idx_paquete_viaje       ON paquete(id_viaje);
CREATE INDEX IF NOT EXISTS idx_unidad_turno        ON unidad(id_turno);
