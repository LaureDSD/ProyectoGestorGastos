package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.usuario.TipoUsuario;
import Proyecto.GestorAPI.models.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> getByTipoUsuario(TipoUsuario tipo);
    Usuario findByCorreo(String username);
}