package Proyecto.GestorAPI.security;

import Proyecto.GestorAPI.security.oauth2.OAuth2Provider;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * Clase que implementa tanto {@link UserDetails} como {@link OAuth2User},
 * representando un usuario dentro del sistema que puede autenticarse tanto
 * de manera tradicional (usuario y contraseña) como a través de OAuth2
 * (por ejemplo, Google, GitHub, etc.).
 *
 * {@link CustomUserDetails} encapsula toda la información relevante del
 * usuario, incluyendo detalles de autenticación y autorización, así como los
 * atributos específicos de un usuario autenticado mediante OAuth2.
 *
 * La anotación {@link Data} de Lombok genera automáticamente los métodos getter, setter,
 * equals, hashCode y toString, facilitando el acceso y manipulación de los atributos.
 */
@Data
public class CustomUserDetails implements OAuth2User, UserDetails {

    /**
     * ID único del usuario en la base de datos.
     */
    private Long id;

    /**
     * Nombre de usuario para la autenticación, utilizado tanto en el login tradicional como en OAuth2.
     */
    private String username;

    /**
     * Contraseña del usuario, utilizada en la autenticación tradicional.
     * En el caso de OAuth2, este campo puede no ser relevante.
     */
    private String password;

    /**
     * Nombre completo del usuario. Este dato puede provenir del proveedor OAuth2 (por ejemplo, Google).
     */
    private String name;

    /**
     * Correo electrónico del usuario, usado en el proceso de autenticación y notificaciones.
     * En el caso de OAuth2, se puede obtener de los atributos del proveedor (por ejemplo, Google).
     */
    private String email;

    /**
     * URL de la imagen de perfil del usuario, generalmente proporcionada por el proveedor OAuth2.
     */
    private String avatarUrl;

    /**
     * Proveedor de autenticación OAuth2 utilizado (por ejemplo, GOOGLE, GITHUB).
     * Esto se utiliza para identificar de dónde proviene la autenticación.
     */
    private OAuth2Provider provider;

    /**
     * Colección de las autoridades (roles) del usuario. Estos roles determinan los permisos y accesos en la aplicación.
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Atributos adicionales del usuario, generalmente obtenidos del proveedor OAuth2 (por ejemplo, nombre, correo, etc.).
     */
    private Map<String, Object> attributes;

}
