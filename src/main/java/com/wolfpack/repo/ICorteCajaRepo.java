package com.wolfpack.repo;

import com.wolfpack.model.CorteCaja;
import com.wolfpack.model.enums.EstadoCorte;
import com.wolfpack.model.enums.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICorteCajaRepo extends IGenericRepo<CorteCaja, Long> {
    Optional<CorteCaja> findByTerminalAndEstado(Terminal terminal, EstadoCorte estado);


}
