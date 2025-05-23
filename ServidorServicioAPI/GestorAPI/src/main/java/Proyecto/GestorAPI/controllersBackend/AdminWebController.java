package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.modelsDTO.ServerInfoDto;
import Proyecto.GestorAPI.servicesimpl.ServerStatsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    private final ServerStatsServiceImpl serverStatsService;

    public AdminWebController(ServerStatsServiceImpl serverStatsService) {
        this.serverStatsService = serverStatsService;
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model, Authentication authentication) {
        // Aquí puedes usar authentication para agregar info del usuario si quieres mostrar en la vista
        ServerInfoDto serverInfo = serverStatsService.getFullServerInfo();
        model.addAttribute("serverInfo", serverInfo);

        // Opcional: agregar roles o nombre del admin autenticado al modelo para usar en la vista
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("roles", authentication.getAuthorities());
        }

        return "admin/dashboard"; // nombre de la plantilla Thymeleaf u otro motor
    }
}
