package Proyecto.GestorAPI.controllers.securityController;

import Proyecto.GestorAPI.exceptions.DuplicatedUserInfoException;
import Proyecto.GestorAPI.exceptions.UserBlockedException;
import Proyecto.GestorAPI.exceptions.UserNotFoundException;
import Proyecto.GestorAPI.modelsDTO.authDTO.AuthResponse;
import Proyecto.GestorAPI.modelsDTO.authDTO.LoginRequest;
import Proyecto.GestorAPI.modelsDTO.authDTO.SignUpRequest;
import Proyecto.GestorAPI.services.UserService;
import Proyecto.GestorAPI.servicesimpl.AuthServiceImpl;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

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
    private final LoginAttemptServiceImpl loginAttemptService;
    @Autowired
    private AuthServiceImpl authService;

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
    @CrossOrigin(origins = "http://localhost:4200")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        if (loginAttemptService.isBlocked(loginRequest.user())) {
            loginAttemptService.registerLoginAttempt(loginRequest.user(), false);
            throw new UserBlockedException("Cuenta bloqueada temporalmente. Intente nuevamente en 30 minutos");
        }

        try {
            String token = authService.authenticateAndGetToken(loginRequest.user(), loginRequest.password());
            loginAttemptService.registerLoginAttempt(loginRequest.user(), true);
            return new AuthResponse(token);
        }catch (AuthenticationException | UserNotFoundException | UserBlockedException ex) {
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
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.hasUserWithUsername(signUpRequest.username())) {
            throw new DuplicatedUserInfoException(String.format("Username %s already been used", signUpRequest.username()));
        }
        if (userService.hasUserWithEmail(signUpRequest.email())) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", signUpRequest.email()));
        }
        userService.saveUser(authService.mapSignUpRequestToUser(signUpRequest));
        String token = authService.authenticateAndGetToken(signUpRequest.username(), signUpRequest.password());
        return new AuthResponse(token);
    }

}
