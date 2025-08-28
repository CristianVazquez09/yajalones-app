package com.wolfpack.repo;

import com.wolfpack.model.Usuario;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUsuarioRepo extends IGenericRepo<Usuario, Integer> {

    Usuario findOneByNombreUsuario(String nombreUsuario);

    boolean existsUsuarioByNombreUsuario(String nombreUsuario);

    @Query("select u.idUsuario from Usuario u where u.nombreUsuario = :nombreUsuario")
    Optional<Integer> findIdByNombreUsuario(String nombreUsuario);

}
