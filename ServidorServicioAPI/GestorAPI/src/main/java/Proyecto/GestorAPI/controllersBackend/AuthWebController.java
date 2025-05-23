package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.authDTO.AuthResponse;
import Proyecto.GestorAPI.modelsDTO.authDTO.JwtResponse;
import Proyecto.GestorAPI.modelsDTO.authDTO.SignUpRequest;
import Proyecto.GestorAPI.servicesimpl.AuthServiceImpl;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import Proyecto.GestorAPI.servicesimpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador web encargado de manejar las rutas de autenticación,
 * registro y cierre de sesión de usuarios.
 *
 * <p>
 * Provee endpoints para:
 * <ul>
 *     <li>Mostrar formulario de registro</li>
 *     <li>Registrar usuarios</li>
 *     <li>Mostrar formulario de login</li>
 *     <li>Login vía token para autenticación API REST</li>
 *     <li>Cerrar sesión de usuarios</li>
 * </ul>
 * </p>
 *
 * <p>
 * La clase utiliza servicios de autenticación, manejo de usuarios y
 * registro de intentos de login para controlar el flujo de autenticación.
 * </p>
 *
 * Ruta base: <code>/auth</code>
 */
@Controller
@RequestMapping("/auth")
public class AuthWebController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl usuarioService;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    /**
     * Endpoint GET para mostrar el formulario de registro de usuarios.
     *
     * @return nombre de la plantilla Thymeleaf "register.html" que contiene el formulario.
     */
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    /**
     * Endpoint POST para procesar el registro de un nuevo usuario.
     *
     * <p>
     * Recibe los datos del formulario mapeados en un objeto {@link SignUpRequest} y
     * realiza el guardado del usuario usando los servicios correspondientes.
     * </p>
     *
     * <p>
     * En caso de error en el guardado, agrega un mensaje de error al modelo y vuelve
     * a mostrar el formulario de registro.
     * </p>
     *
     * @param signUpRequest DTO con datos enviados desde el formulario de registro.
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return redirección a la página de login con parámetro de éxito o el formulario de registro con error.
     */
    @PostMapping("/register")
    public String register(@ModelAttribute SignUpRequest signUpRequest, Model model) {
        try {
            if (usuarioService.saveUser(authService.mapSignUpRequestToUser(signUpRequest)) == null) {
                model.addAttribute("error", "Error al guardar usuario");
                return "register";
            }
            return "redirect:/auth/login?registroExitoso=true";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "register";
        }
    }

    /**
     * Endpoint GET para mostrar el formulario de login web.
     *
     * @return nombre de la plantilla Thymeleaf "login.html" que contiene el formulario de login.
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * Endpoint POST para autenticación vía token (API REST).
     *
     * <p>
     * Recibe parámetros "correo" y "contraseña" para autenticar el usuario.
     * </p>
     *
     * <p>
     * En caso de éxito, genera un token JWT y registra el intento de login exitoso.
     * Retorna un JSON con el token.
     * </p>
     *
     * <p>
     * En caso de error de autenticación, registra el intento fallido y retorna un
     * código 401 con mensaje de error.
     * </p>
     *
     * @param correo correo electrónico del usuario
     * @param contraseña contraseña del usuario
     * @param request objeto {@link HttpServletRequest} (no usado internamente)
     * @return {@link ResponseEntity} con el token JWT o error de autenticación.
     */
    @PostMapping("/loginToken")
    public ResponseEntity<?> login(@RequestParam String correo, @RequestParam String contraseña, HttpServletRequest request) {
        try {
            System.out.println(correo + " " + contraseña);
            String token = authService.authenticateAndGetToken(correo, contraseña);
            loginAttemptService.registerLoginAttempt(correo, true);
            System.out.println(Map.of("token", token));
            return ResponseEntity.ok().body(Map.of("token", token));
        } catch (AuthenticationException ex) {
            loginAttemptService.registerLoginAttempt(correo, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Error en la autenticación:" + ex));
        }
    }

    /**
     * Endpoint POST para cerrar sesión del usuario.
     *
     * <p>
     * Invoca el handler de logout de Spring Security para limpiar el contexto de seguridad y la sesión.
     * Luego redirige al login con un parámetro que indica que se realizó el logout.
     * </p>
     *
     * @param request objeto {@link HttpServletRequest} utilizado para la sesión actual.
     * @param response objeto {@link HttpServletResponse} para manejar la respuesta HTTP.
     * @return redirección a la página de login con parámetro logout.
     */
    @PostMapping("/logout")
    public String performLogout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
        return "redirect:/login?logout";
    }
}
