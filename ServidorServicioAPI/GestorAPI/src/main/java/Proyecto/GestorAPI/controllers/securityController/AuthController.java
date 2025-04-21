package Proyecto.GestorAPI.controllers.securityController;

import Proyecto.GestorAPI.exceptions.DuplicatedUserInfoException;
import Proyecto.GestorAPI.exceptions.UserBlockedException;
import Proyecto.GestorAPI.exceptions.UserNoFoundException;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.security.RoleServer;
import Proyecto.GestorAPI.modelsDTO.authDTO.AuthResponse;
import Proyecto.GestorAPI.modelsDTO.authDTO.LoginRequest;
import Proyecto.GestorAPI.modelsDTO.authDTO.SignUpRequest;
import Proyecto.GestorAPI.security.TokenProvider;
import Proyecto.GestorAPI.security.oauth2.OAuth2Provider;
import Proyecto.GestorAPI.services.UserService;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controlador encargado de gestionar la autenticación y el registro de usuarios.
 *
 * Este controlador proporciona las siguientes funcionalidades:
 * - **Autenticación** de usuarios con su nombre de usuario y contraseña, devolviendo un token JWT.
 * - **Registro** de nuevos usuarios, asegurando que no existan duplicados de nombre de usuario o correo electrónico.
 *
 * Swagger Documentation:
 * - Las rutas `/authenticate` y `/signup` están documentadas para permitir la interacción a través de la API con ejemplos de datos esperados.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Register and Login (Open)", description = "Crea cuenta o identifcate")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final LoginAttemptServiceImpl loginAttemptService;

    /**
     * Endpoint para la autenticación de usuarios.
     * Este endpoint recibe un nombre de usuario y una contraseña, valida las credenciales
     * y devuelve un token JWT para el acceso posterior.
     *
     * @param loginRequest Contiene las credenciales del usuario (nombre de usuario y contraseña).
     * @return Devuelve el token JWT en un objeto `AuthResponse`.
     */
    @Operation(summary = "Autenticar un usuario",
            description = "Permite a un usuario autenticarse proporcionando su nombre de usuario/email y contraseña, " +
                    "y obtener un token JWT para acceder a las rutas protegidas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
            })
    @PostMapping("/authenticate")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {

        // Primero, verifica si el usuario está bloqueado
        if (loginAttemptService.isBlocked(loginRequest.user())) {
            throw new UserBlockedException("Cuenta bloqueada temporalmente. Intente nuevamente en 30 minutos");
        }

        try {
            String token = authenticateAndGetToken(loginRequest.user(), loginRequest.password());
            // Registra el intento exitoso (podrías limpiar los registros de fallos para ese usuario si se desea)
            loginAttemptService.registerLoginAttempt(loginRequest.user(), true);
            return new AuthResponse(token);
        } catch (AuthenticationException ex) {
            // Registra el intento fallido
            loginAttemptService.registerLoginAttempt(loginRequest.user(), false);
            throw ex;
        }

    }

    /**
     * Endpoint para registrar a un nuevo usuario.
     * Este endpoint recibe los datos de un nuevo usuario, verifica que el nombre de usuario y el correo electrónico
     * no estén en uso, luego crea el usuario y devuelve un token JWT para el acceso del nuevo usuario.
     *
     * @param signUpRequest Contiene los datos necesarios para el registro del usuario.
     * @return Devuelve el token JWT en un objeto `AuthResponse`.
     * @throws DuplicatedUserInfoException Si el nombre de usuario o el correo electrónico ya están registrados.
     */
    @Operation(summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario asegurándose de que no haya duplicados de nombre de usuario " +
                    "o correo electrónico, y devuelve un token JWT para acceder a la aplicación.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Registro exitoso",
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Usuario o correo electrónico ya registrados")
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        // Verifica si el nombre de usuario ya está en uso
        if (userService.hasUserWithUsername(signUpRequest.username())) {
            throw new DuplicatedUserInfoException(String.format("Username %s already been used", signUpRequest.username()));
        }
        // Verifica si el correo electrónico ya está en uso
        if (userService.hasUserWithEmail(signUpRequest.email())) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", signUpRequest.email()));
        }

        // Guarda el nuevo usuario
        userService.saveUser(mapSignUpRequestToUser(signUpRequest));

        // Genera un token para el nuevo usuario
        String token = authenticateAndGetToken(signUpRequest.username(), signUpRequest.password());
        return new AuthResponse(token);
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
    private String authenticateAndGetToken(String user, String password) {
        Authentication authentication;

        // Obtiene el usuario ya sea por nombre de usuario o por correo electrónico
        Optional<User> userOptional = userService.getUserByUsernameOrEmail(user);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            // Realiza la autenticación del usuario con el `AuthenticationManager`
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(existingUser.getUsername(), password));
            return tokenProvider.generate(authentication);
        } else {
            throw new UserNoFoundException("User not found");
        }
    }

    /**
     * Mapea un objeto `SignUpRequest` a un objeto `User`, para crear un nuevo usuario.
     *
     * @param signUpRequest Los datos proporcionados por el usuario para el registro.
     * @return El objeto `User` que se utilizará para guardar el nuevo usuario en la base de datos.
     */
    private User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.username());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setName(signUpRequest.name());
        user.setEmail(signUpRequest.email());
        user.setRole(RoleServer.USER); // El rol por defecto es "USER"
        user.setProvider(OAuth2Provider.LOCAL); // El proveedor es LOCAL por defecto
        return user;
    }
}
