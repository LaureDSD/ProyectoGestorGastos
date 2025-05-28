package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.exceptions.ErrorConexionServidorException;
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
        ServerInfoDto serverInfo = new ServerInfoDto();
        try {
            serverInfo = serverStatsService.getFullServerInfo();
        }catch (Exception | ErrorConexionServidorException e){
            serverInfo.setInfo(e.getMessage());
            serverInfo.setActiveapi(false);
            serverInfo.setActiveocr(false);
        }

        model.addAttribute("serverInfo", serverInfo);

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("roles", authentication.getAuthorities());
        }

        return "admin/dashboard";
    }
}
