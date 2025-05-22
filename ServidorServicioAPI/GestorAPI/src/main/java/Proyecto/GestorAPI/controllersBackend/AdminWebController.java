package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.modelsDTO.ServerInfoDto;
import Proyecto.GestorAPI.servicesimpl.ServerStatsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    private final ServerStatsServiceImpl serverStatsService;

    public AdminWebController(ServerStatsServiceImpl serverStatsService) {
        this.serverStatsService = serverStatsService;
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model, Authentication authentication) {
        ServerInfoDto serverInfo = serverStatsService.getFullServerInfo();
        model.addAttribute("serverInfo", serverInfo);
        return "admin/dashboard";
    }
}


