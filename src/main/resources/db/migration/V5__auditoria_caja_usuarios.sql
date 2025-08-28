-- Auditar movimientos y cortes con usuario (FK a usuario.id_usuario)
ALTER TABLE movimiento_caja
    ADD COLUMN IF NOT EXISTS created_by INTEGER,
    ADD COLUMN IF NOT EXISTS created_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS last_modified_by INTEGER,
    ADD COLUMN IF NOT EXISTS last_modified_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS recibo_folio VARCHAR(32),
    ADD CONSTRAINT fk_mc_created_by FOREIGN KEY (created_by) REFERENCES usuario(id_usuario),
    ADD CONSTRAINT fk_mc_last_modified_by FOREIGN KEY (last_modified_by) REFERENCES usuario(id_usuario);

ALTER TABLE corte_caja
    ADD COLUMN IF NOT EXISTS created_by INTEGER,
    ADD COLUMN IF NOT EXISTS created_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS last_modified_by INTEGER,
    ADD COLUMN IF NOT EXISTS last_modified_date TIMESTAMP,
    ADD COLUMN IF NOT EXISTS abierto_por INTEGER,
    ADD COLUMN IF NOT EXISTS cerrado_por INTEGER,
    ADD CONSTRAINT fk_cc_created_by FOREIGN KEY (created_by) REFERENCES usuario(id_usuario),
    ADD CONSTRAINT fk_cc_last_modified_by FOREIGN KEY (last_modified_by) REFERENCES usuario(id_usuario),
    ADD CONSTRAINT fk_cc_abierto_por FOREIGN KEY (abierto_por) REFERENCES usuario(id_usuario),
    ADD CONSTRAINT fk_cc_cerrado_por FOREIGN KEY (cerrado_por) REFERENCES usuario(id_usuario);
