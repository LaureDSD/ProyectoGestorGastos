package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.services.StorageService;
import Proyecto.GestorAPI.servicesimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador web para la gestión de usuarios en el área administrativa.
 *
 * Proporciona funcionalidades para listar, editar, crear, guardar y eliminar usuarios.
 * Utiliza Thymeleaf para la representación de vistas HTML.
 *
 * Mapea las URLs bajo "/admin/usuarios".
 */
@Controller
@RequestMapping("/admin/usuarios")
public class UserWebController {

    /**
     * Ruta común para las vistas HTML relacionadas con usuarios.
     */
    private final String rutaHTML = "admin/usuarios";

    @Autowired
    private UserServiceImpl usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StorageService storageService;

    /**
     * Lista de roles disponibles para asignar a usuarios.
     */
    private List<RoleServer> tl;

    /**
     * Endpoint GET para mostrar la lista completa de usuarios.
     * Añade al modelo la lista de usuarios, un nuevo usuario vacío para formularios,
     * y la lista de roles disponibles.
     *
     * @param model Modelo para pasar atributos a la vista.
     * @return Nombre de la plantilla HTML para mostrar usuarios.
     */
    @GetMapping
    public String showUsuariosList(Model model) {
        try {
            tl = List.of(RoleServer.values());
            List<User> usuarios = usuarioService.getUsers();
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("usuario", new User());
            model.addAttribute("roleList", tl);
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los usuarios: " + e.getMessage());
            return rutaHTML;
        }
    }

    /**
     * Endpoint GET para mostrar el formulario de edición de un usuario.
     * Carga el usuario por id o crea uno nuevo si no se encuentra.
     * La contraseña se oculta en la vista para evitar su visualización directa.
     *
     * @param id Identificador del usuario a editar.
     * @param model Modelo para pasar atributos a la vista.
     * @return Nombre de la plantilla HTML para editar un usuario.
     */
    @GetMapping("/edit/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model) {
        try {
            User usuario = usuarioService.getUserById(id).orElse(new User());
            model.addAttribute("roleList", tl);
            model.addAttribute("usuario", usuario);
            usuario.setPassword("*******");  // Oculta la contraseña en el formulario
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el usuario para editar: " + e.getMessage());
            return rutaHTML;
        }
    }

    /**
     * Endpoint POST para guardar un usuario, ya sea creación o actualización.
     * En caso de edición, si el campo contraseña está vacío o es igual a "*******",
     * se mantiene la contraseña previa; si no, se encripta la nueva contraseña.
     * En caso de creación, la contraseña se guarda tal cual fue ingresada.
     *
     * @param usuario Objeto User enviado desde el formulario.
     * @param model Modelo para pasar atributos a la vista.
     * @return Redirección a la lista de usuarios.
     */
    @PostMapping("/save")
    public String saveUsuario(@ModelAttribute User usuario, Model model) {
        if (usuario.getId() != null) { // Edición
            User existingUsuario = usuarioService.getUserById(usuario.getId()).orElse(null);

            // Mantiene la contraseña existente si el campo está vacío o es "*******"
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty() || usuario.getPassword().equals("*******")) {
                usuario.setPassword(existingUsuario.getPassword());
            } else {
                // Encripta la contraseña
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }

            //Gestionar img
            if(usuario.getImageUrl() == null || usuario.getImageUrl().trim().isEmpty()){
                if(existingUsuario.getImageUrl()!=null || existingUsuario.getImageUrl().trim().isEmpty()) {
                    storageService.deleteImageData(existingUsuario.getImageUrl());
                    usuario.setImageUrl(null);
                }
            } else if (!existingUsuario.getImageUrl().equals(usuario.getImageUrl())) {
                storageService.deleteImageData(existingUsuario.getImageUrl());
            }


        } else { // Creación
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        usuarioService.saveUser(usuario);
        return "redirect:/" + rutaHTML;
    }

    /**
     * Endpoint GET para eliminar un usuario por su id.
     * Redirige a la lista de usuarios o muestra mensaje de error si falla.
     *
     * @param id Identificador del usuario a eliminar.
     * @param model Modelo para pasar atributos a la vista.
     * @return Redirección a la lista de usuarios o recarga con error.
     */
    @GetMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id, Model model) {
        try {
            User user = usuarioService.getUserById(id).orElse(null);
            if(user!=null) {
                storageService.deleteImageData(user.getImageUrl());
                System.out.println(user.getImageUrl());
                usuarioService.deleteUser(user);
            }else{
                model.addAttribute("error", "Error al eliminar el usuario: ");
            }
            return "redirect:/" + rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
            return rutaHTML;
        }
    }
}
