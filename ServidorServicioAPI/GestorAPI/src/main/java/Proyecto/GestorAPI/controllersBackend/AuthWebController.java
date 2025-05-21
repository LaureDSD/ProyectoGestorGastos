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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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


    // Método GET para mostrar el formulario de registro
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Retorna la plantilla register.html
    }

    // Método POST para manejar el registro
    @PostMapping("/register")
    public String register(@ModelAttribute SignUpRequest signUpRequest, Model model) {
        try {
            if (usuarioService.saveUser(authService.mapSignUpRequestToUser(signUpRequest)) == null) {
                model.addAttribute("error", "Error al guardar usuario");
                return "register"; // Muestra el formulario de registro con el mensaje de error
            }
            return "redirect:/auth/login?registroExitoso=true"; // Redirige al login después del registro
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "register"; // Muestra el formulario de registro con el mensaje de error
        }
    }

    // Método GET para mostrar el formulario de login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Retorna la plantilla login.html
    }


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

    @PostMapping("/logout")
    public String performLogout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
        return "redirect:/login?logout";
    }
}
