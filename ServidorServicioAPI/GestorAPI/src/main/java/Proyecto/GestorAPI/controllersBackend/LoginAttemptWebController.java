package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/admin/login-attempts")
public class LoginAttemptWebController {

    private final String rutaHTML = "/admin/login-attempts";

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    // Listar todos los intentos de login
    @GetMapping
    public String listarIntentosLogin(Model model,
                                      @RequestParam(required = false) String username) {
        try {
            List<LoginAttempt> intentos;

            if (username != null ) {
                // Búsqueda filtrada
                intentos = loginAttemptService.getByUsernameOrEamil(username);
            } else {
                // Todos los registros
                intentos = loginAttemptService.getAll();
            }

            model.addAttribute("attempts", intentos);
            model.addAttribute("searchUsername", username);
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los intentos de login: " + e.getMessage());
            return rutaHTML;
        }
    }




}