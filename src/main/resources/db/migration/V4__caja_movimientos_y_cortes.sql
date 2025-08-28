-- Terminal/caja por turno con totales persistidos al cerrar
CREATE TABLE IF NOT EXISTS corte_caja (
    id_corte            SERIAL PRIMARY KEY,
    terminal            VARCHAR(20)  NOT NULL,                -- 'TUXTLA' | 'YAJALON' | 'SCLC'
    turno_label         VARCHAR(50)  NOT NULL,                -- texto del turno (ej. 'MATUTINO' o id)
    apertura            TIMESTAMP    NOT NULL DEFAULT now(),
    cierre              TIMESTAMP,
    estado              VARCHAR(20)  NOT NULL,                -- 'ABIERTO' | 'CERRADO'
    ingresos            NUMERIC(12,2) NOT NULL DEFAULT 0,
    pendientes_creados  NUMERIC(12,2) NOT NULL DEFAULT 0,
    pendientes_cobrados NUMERIC(12,2) NOT NULL DEFAULT 0,
    saldo_inicial_pend  NUMERIC(12,2) NOT NULL DEFAULT 0,
    saldo_final_pend    NUMERIC(12,2) NOT NULL DEFAULT 0,
    CONSTRAINT uq_corte_abierto UNIQUE (terminal, estado)
    DEFERRABLE INITIALLY IMMEDIATE
    );

-- Movimientos de caja por terminal
CREATE TABLE IF NOT EXISTS movimiento_caja (
    id_movimiento       SERIAL PRIMARY KEY,
    terminal            VARCHAR(20)  NOT NULL,                -- 'TUXTLA' | 'YAJALON' | 'SCLC'
    tipo                VARCHAR(20)  NOT NULL,                -- 'INGRESO' | 'PENDIENTE' | 'COBRO_PENDIENTE'
    categoria           VARCHAR(20)  NOT NULL,                -- 'PASAJERO' | 'PAQUETE'
    importe             NUMERIC(12,2) NOT NULL CHECK (importe >= 0),
    id_viaje            INTEGER      NOT NULL REFERENCES viaje(id_viaje),
    id_referencia       INTEGER      NOT NULL,                -- id_pasajero o id_paquete
    id_pendiente_origen INTEGER,                               -- FK lógica al pendiente original
    creado_en           TIMESTAMP    NOT NULL DEFAULT now(),
    saldado             BOOLEAN      NOT NULL DEFAULT false,   -- solo aplica a PENDIENTE
    saldado_en          TIMESTAMP,
    descripcion         TEXT
    );

CREATE INDEX IF NOT EXISTS idx_mc_terminal_fecha ON movimiento_caja(terminal, creado_en);
CREATE INDEX IF NOT EXISTS idx_mc_tipo_terminal ON movimiento_caja(tipo, terminal);
CREATE INDEX IF NOT EXISTS idx_mc_pendiente_ref ON movimiento_caja(id_referencia, tipo);
