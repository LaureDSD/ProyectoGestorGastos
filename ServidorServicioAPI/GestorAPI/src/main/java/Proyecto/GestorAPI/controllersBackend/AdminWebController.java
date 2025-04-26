package Proyecto.GestorAPI.controllersBackend;

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

    @GetMapping("/dashboard")
    public String getDashboard(Model model, Authentication authentication) {
        if (authentication != null) {
            System.out.println("redirect");
        }
        System.out.println("redirect2");

        return "admin/dashboard";
    }
}


