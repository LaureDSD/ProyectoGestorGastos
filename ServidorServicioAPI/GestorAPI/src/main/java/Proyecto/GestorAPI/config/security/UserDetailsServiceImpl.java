package Proyecto.GestorAPI.config.security;

import Proyecto.GestorAPI.models.usuario.Usuario;
import Proyecto.GestorAPI.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> optionalUsuario = Optional.ofNullable(usuarioRepository.findByCorreo(username));
        if (optionalUsuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        Usuario usuario = optionalUsuario.get();

        // Obtener el nombre del enum como autoridad
        GrantedAuthority authority = new SimpleGrantedAuthority(usuario.getTipoUsuario().name());

        return new User(
                usuario.getCorreo(), usuario.getContraseña(), List.of(authority));
    }
}
