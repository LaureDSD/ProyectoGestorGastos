package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.exceptions.UserBlockedException;
import Proyecto.GestorAPI.exceptions.UserNotFoundException;
import Proyecto.GestorAPI.models.Contacto;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.authDTO.SignUpRequest;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.config.security.TokenProvider;
import Proyecto.GestorAPI.config.security.oauth2.OAuth2Provider;
import Proyecto.GestorAPI.services.AuthService;
import Proyecto.GestorAPI.services.ContactoService;
import Proyecto.GestorAPI.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private static long localUserCounter = 0;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactoService contactoService;

    final AuthenticationManager authenticationManager;
    final TokenProvider tokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostConstruct
    public void init() {
        localUserCounter = userService.countByProvider(OAuth2Provider.LOCAL);
    }

    /**
     * Método privado para autenticar al usuario con las credenciales proporcionadas
     * y obtener un token JWT.
     *
     * @param user El nombre de usuario.
     * @param password La contraseña del usuario.
     * @return El token JWT generado para el usuario autenticado.
     * @throws RuntimeException Si el usuario no es encontrado en el sistema.
     */
    public String authenticateAndGetToken(String user, String password) {
        Authentication authentication;
        Optional<User> userOptional = userService.getUserByUsernameOrEmail(user);
        if (userOptional.isPresent() ) {
            if(userOptional.get().isActive()) {
                User existingUser = userOptional.get();
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(existingUser.getUsername(), password));
                System.out.println(authentication);
                return tokenProvider.generate(authentication);
            } else  {
                throw new UserBlockedException("The account is blocked or pending deletion");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Mapea un objeto `SignUpRequest` a un objeto `User`, para crear un nuevo usuario.
     *
     * @param signUpRequest Los datos proporcionados por el usuario para el registro.
     * @return El objeto `User` que se utilizará para guardar el nuevo usuario en la base de datos.
     */
    public User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.username());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
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

    @Override
    public void processForgotPassword(String email) throws Exception {
        /*Optional<User> optionalUser = userService.getUserByEmail(email);

        if (optionalUser.isEmpty()) {
            return;
        }*/

        //Por no implementar el emailservice
        Contacto contacto = new Contacto();
        contacto.setAsunto("RECUPERACION");
        contacto.setRevisado(Boolean.FALSE);
        contacto.setCorreo(email);
        contacto.setMensaje("RECUPERACION{" +
                "mail{"+email+"}" +
                //optionalUser +
                "}");

        contactoService.setItem(contacto);

        System.out.println("Recuperación para " + email );
    }
}
