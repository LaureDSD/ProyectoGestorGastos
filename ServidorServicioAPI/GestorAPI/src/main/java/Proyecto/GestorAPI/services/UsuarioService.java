package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.usuario.TipoUsuario;
import Proyecto.GestorAPI.models.usuario.Usuario;
import Proyecto.GestorAPI.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<Usuario> getAll(){
        return  usuarioRepository.findAll();
    }

    public Usuario getByID(Long id){
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario setItem(Usuario o){
        o.setContraseña(new BCryptPasswordEncoder().encode(o.getContraseña()));
        return  usuarioRepository.save(o);
    }

    public  void deleteByID(Long id){
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> getByTipoUsuario(TipoUsuario tu) {
        return usuarioRepository.getByTipoUsuario(tu);
    }
}
