package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.log.LogUsuario;
import Proyecto.GestorAPI.models.log.TipoLog;
import Proyecto.GestorAPI.repositories.LogUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogUsuarioService {
    @Autowired
    private LogUsuarioRepository logRepository;

    public List<LogUsuario> getAll(){
        return  logRepository.findAll();
    }

    public LogUsuario getByID(long id){
        return logRepository.findById(id).orElse(null);
    }

    public LogUsuario setItem(LogUsuario o){
        return  logRepository.save(o);
    }

    public  void deleteByID(long id){
        logRepository.deleteById(id);
    }

    public List<LogUsuario> getBytipoLog(TipoLog tipoLog) {
        return logRepository.getBytipoLog(tipoLog);
    }
}
