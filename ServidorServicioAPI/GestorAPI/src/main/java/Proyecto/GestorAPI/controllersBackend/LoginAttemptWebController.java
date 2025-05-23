package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/login-attempts")
public class LoginAttemptWebController {

    private final String rutaHTML = "/admin/login-attempts";

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;


    private void initDatosCompartidos() {

    }

    @GetMapping
    public String listarIntentosLogin(Model model) {
        initDatosCompartidos(); // Para futuro uso

        try {
            List<LoginAttempt> intentos = loginAttemptService.getAll();
            model.addAttribute("attempts", intentos);
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los intentos de login: " + e.getMessage());
        }

        return rutaHTML;
    }
}
