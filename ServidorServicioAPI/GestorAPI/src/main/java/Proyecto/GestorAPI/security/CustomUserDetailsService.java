package Proyecto.GestorAPI.security;

import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Implementación personalizada de {@link UserDetailsService} que carga los detalles
 * del usuario desde la base de datos a través del servicio {@link UserService}.
 *
 * Esta clase se encarga de convertir un {@link User} (entidad) en un objeto {@link CustomUserDetails},
 * que incluye la información del usuario junto con los roles de seguridad (autoridades) necesarios
 * para la autenticación y autorización en la aplicación.
 *
 * La anotación {@link RequiredArgsConstructor} de Lombok genera automáticamente un constructor
 * con todos los atributos `final` de la clase, simplificando la inyección de dependencias.
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Servicio que gestiona las operaciones relacionadas con los usuarios en la base de datos.
     */
    private final UserService userService;

    /**
     * Método sobrescrito de {@link UserDetailsService} que carga los detalles de un usuario
     * a partir de su nombre de usuario. Lanza una excepción {@link UsernameNotFoundException}
     * si el usuario no se encuentra en la base de datos.
     *
     * @param username Nombre de usuario utilizado para la búsqueda.
     * @return {@link UserDetails} con la información del usuario.
     * @throws UsernameNotFoundException si el nombre de usuario no se encuentra.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Obtiene el usuario desde la base de datos a través del servicio
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));

        // Crea una lista de autoridades (roles) basada en el rol del usuario
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));

        // Mapea el usuario a un objeto CustomUserDetails
        return mapUserToCustomUserDetails(user, authorities);
    }

    /**
     * Mapea un {@link User} a un {@link CustomUserDetails}, configurando los datos del usuario
     * y las autoridades de seguridad.
     *
     * @param user       El objeto {@link User} que contiene los detalles del usuario.
     * @param authorities Lista de autoridades (roles) del usuario.
     * @return Un objeto {@link CustomUserDetails} con los datos del usuario y sus autoridades.
     */
    private CustomUserDetails mapUserToCustomUserDetails(User user, List<SimpleGrantedAuthority> authorities) {
        // Crea un objeto CustomUserDetails y asigna los valores del usuario
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setId(user.getId());
        customUserDetails.setUsername(user.getUsername());
        customUserDetails.setPassword(user.getPassword());
        customUserDetails.setName(user.getName());
        customUserDetails.setEmail(user.getEmail());
        customUserDetails.setAuthorities(authorities);
        return customUserDetails;
    }
}
