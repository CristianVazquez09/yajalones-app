package com.wolfpack.repo;

import com.wolfpack.model.Usuario;

public interface IUsuarioRepo extends IGenericRepo<Usuario, Integer> {

    Usuario findOneByNombreUsuario(String nombreUsuario);

    boolean existsUsuarioByNombreUsuario(String nombreUsuario);
}
