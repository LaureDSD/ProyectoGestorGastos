package Proyecto.GestorAPI.controllersBackend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    //Al acceder a la raiz del servidor, accedes al login
    @GetMapping("/")
    public String home() {
        return "login";
    }
}
