package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.exceptions.UserBlockedException;
import Proyecto.GestorAPI.exceptions.UserNotFoundException;
import Proyecto.GestorAPI.models.FormContacto;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.authDTO.SignUpRequest;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.config.security.TokenProvider;
import Proyecto.GestorAPI.config.security.oauth2.OAuth2Provider;
import Proyecto.GestorAPI.services.AuthService;
import Proyecto.GestorAPI.services.FormContactoService;
import Proyecto.GestorAPI.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación del servicio de autenticación que maneja la lógica de login,
 * registro y recuperación de contraseña.
 *
 * Este servicio utiliza Spring Security para autenticar usuarios y generar tokens JWT,
 * además de mapear los datos de registro y manejar solicitudes de recuperación de contraseña.
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Contador local para usuarios registrados vía proveedor LOCAL.
     * Se inicializa al iniciar el servicio con el conteo actual.
     */
    private static long localUserCounter = 0;

    /**
     * Codificador de contraseñas para asegurar almacenamiento seguro.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Servicio para la gestión de usuarios.
     */
    @Autowired
    private UserService userService;

    /**
     * Servicio para gestionar formularios de contacto (usado en recuperación de contraseña).
     */
    @Autowired
    private FormContactoService contactoService;

    /**
     * Administrador de autenticación de Spring Security para validar credenciales.
     */
    final AuthenticationManager authenticationManager;

    /**
     * Proveedor de tokens JWT para generación de tokens tras autenticación.
     */
    final TokenProvider tokenProvider;

    /**
     * Constructor inyectando AuthenticationManager y TokenProvider.
     *
     * @param authenticationManager Componente para autenticación.
     * @param tokenProvider Generador de tokens JWT.
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Método invocado después de la construcción para inicializar el contador
     * de usuarios locales con los datos actuales almacenados.
     */
    @PostConstruct
    public void init() {
        localUserCounter = userService.countByProvider(OAuth2Provider.LOCAL);
    }

    /**
     * Autentica un usuario con su nombre (o email) y contraseña, y genera un token JWT si es exitoso.
     *
     * @param user Nombre de usuario o correo electrónico.
     * @param password Contraseña proporcionada.
     * @return Token JWT generado tras autenticación exitosa.
     * @throws UserBlockedException Si el usuario existe pero está bloqueado o pendiente de eliminación.
     * @throws UserNotFoundException Si el usuario no existe en la base de datos.
     * @throws RuntimeException Si ocurre un error durante la autenticación.
     */
    public String authenticateAndGetToken(String user, String password) {
        Authentication authentication;
        Optional<User> userOptional = userService.getUserByUsernameOrEmail(user);

        if (userOptional.isPresent()) {
            if(userOptional.get().isActive()) {
                User existingUser = userOptional.get();

                // Se crea un token de autenticación con el nombre de usuario y contraseña
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(existingUser.getUsername(), password));

                System.out.println(authentication);
                // Se genera y retorna el token JWT basado en la autenticación exitosa
                return tokenProvider.generate(authentication);
            } else  {
                // Usuario bloqueado o pendiente de eliminación
                throw new UserBlockedException("The account is blocked or pending deletion");
            }
        } else {
            // Usuario no encontrado en la base de datos
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Mapea los datos recibidos en un objeto SignUpRequest a un objeto User para registro.
     *
     * Establece valores por defecto, como el rol, estado activo, proveedor LOCAL,
     * ID de proveedor único y URL de imagen de perfil.
     *
     * @param signUpRequest Datos del registro recibidos del cliente.
     * @return Usuario mapeado listo para persistir.
     */
    public User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.username());
        user.setPassword(passwordEncoder.encode(signUpRequest.password())); // Se codifica la contraseña
        user.setName(signUpRequest.name());
        user.setEmail(signUpRequest.email());
        user.setActive(true);
        user.setRole(RoleServer.USER);
        user.setProvider(OAuth2Provider.LOCAL);
        localUserCounter++;
        user.setProviderId("local-"+localUserCounter);
        user.setImageUrl("default/profile.jpg");
        return user;
    }

    /**
     * Procesa una solicitud de recuperación de contraseña enviando un formulario de contacto
     * que simula la solicitud (no se implementa envío real de email).
     *
     * @param email Correo electrónico para recuperar contraseña.
     * @throws Exception Excepciones genéricas si ocurre algún fallo.
     */
    @Override
    public void processForgotPassword(String email) throws Exception {
        /*
         * Se omite validación si el email existe en base de datos para no filtrar usuarios.
         *
         * Optional<User> optionalUser = userService.getUserByEmail(email);
         * if (optionalUser.isEmpty()) { return; }
         */

        // Crea un formulario de contacto con asunto "RECUPERACION"
        FormContacto contacto = new FormContacto();
        contacto.setAsunto("RECUPERACION");
        contacto.setRevisado(Boolean.FALSE);
        contacto.setCorreo(email);
        contacto.setMensaje("RECUPERACION{" +
                "mail{"+email+"}" +
                //optionalUser +
                "}");

        // Guarda el formulario para su revisión posterior
        contactoService.setItem(contacto);

        System.out.println("Recuperación para " + email );
    }
}
