package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.security.RoleServer;
import Proyecto.GestorAPI.servicesimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/usuario/usuarios")
public class UserWebController {

    private final String rutaHTML ="/admin/usuario/usuarios";

    @Autowired
    private UserServiceImpl usuarioService;

    private List<RoleServer> tl;

    // Endpoint para mostrar la lista de usuarios
    @GetMapping
    public String showUsuariosList(Model model) {
        try {
            tl = Arrays.stream(RoleServer.values()).toList();
            List<User> usuarios = usuarioService.getUsers();
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("usuario", new User());
            model.addAttribute("tipoUsuarioList", tl);
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los usuarios: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Formulario para crear o editar un usuario
    @GetMapping("/edit/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model) {
        try {
            User usuario = usuarioService.getUserById(id).orElse(new User());
            model.addAttribute("tipoUsuarioList", tl);
            model.addAttribute("usuario", usuario);
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el usuario para editar: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Guardar un usuario (creación o actualización)
    @PostMapping("/save")
    public String guardarUsuario(@ModelAttribute("usuario") User usuario, Model model) throws IOException {
        try {
            usuarioService.saveUser(usuario);
            return "redirect:"+rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el usuario: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Endpoint para eliminar un usuario
    @GetMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id, Model model) {
        try {
            usuarioService.deleteUser(usuarioService.getUserById(id).orElse(null));
            return "redirect:"+rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
            return rutaHTML;
        }
    }
}
