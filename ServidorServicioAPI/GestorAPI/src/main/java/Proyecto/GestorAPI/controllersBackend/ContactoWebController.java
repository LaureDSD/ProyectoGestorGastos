package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.Contacto;
import Proyecto.GestorAPI.servicesimpl.ContactoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/contactos")
public class ContactoWebController {

    private final String rutaHTML = "/admin/contactos";

    @Autowired
    private ContactoServiceImpl contactoService;


    // CREATE: Guardar nuevo contacto
    @PostMapping("/guardar")
    public String guardarContacto(@ModelAttribute Contacto contacto) {
        contactoService.setItem(contacto);
        return "redirect:" + rutaHTML;
    }

    // READ: Listar todos
    @GetMapping
    public String listarContactos(Model model) {
        List<Contacto> contactos = contactoService.getAll();
        model.addAttribute("contactos", contactos);
        return rutaHTML;
    }

    // UPDATE: Mostrar formulario edición
    @GetMapping("/editar/{id}")
    public String editarContacto(@PathVariable Long id, Model model) {
        Contacto contacto = contactoService.getByID(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));
        model.addAttribute("contacto", contacto);
        return rutaHTML + "-form"; // Mismo formulario para crear/editar
    }

    // DELETE: Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarContacto(@PathVariable Long id) {
        contactoService.deleteByID(id);
        return "redirect:" + rutaHTML;
    }
}