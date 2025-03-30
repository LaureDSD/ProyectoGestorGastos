package Proyecto.GestorAPI.repositories;


import Proyecto.GestorAPI.models.log.LogUsuario;
import Proyecto.GestorAPI.models.log.TipoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogUsuarioRepository extends JpaRepository<LogUsuario,Long> {
    List<LogUsuario> getBytipoLog(TipoLog tipoLog);
}
