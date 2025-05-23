package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.FormContacto;
import Proyecto.GestorAPI.servicesimpl.ContactoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/formcontacto")
public class FormContactoWebController {

    private final String rutaHTML = "/admin/formcontacto";

    @Autowired
    private ContactoServiceImpl contactoService;

    private List<FormContacto> contactos;

    private void initDatosCompartidos() {
        contactos = contactoService.getAll();
    }

    // READ: Listar todos los contactos
    @GetMapping
    public String listarContactos(Model model) {
        try {
            initDatosCompartidos();
            model.addAttribute("contactos", contactos);
            model.addAttribute("contacto", new FormContacto()); // Para formulario nuevo
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los contactos: " + e.getMessage());
            return rutaHTML;
        }
    }

    // CREATE / UPDATE: Guardar contacto
    @PostMapping("/save")
    public String guardarContacto(@ModelAttribute FormContacto contacto, Model model) {
        try {
            contactoService.setItem(contacto);
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            initDatosCompartidos();
            model.addAttribute("error", "Error al guardar el contacto: " + e.getMessage());
            model.addAttribute("contacto", contacto);
            model.addAttribute("contactos", contactos);
            return rutaHTML;
        }
    }

    // UPDATE: Editar contacto (rellenar formulario)
    @GetMapping("/edit/{id}")
    public String editarContacto(@PathVariable("id") Long id, Model model) {
        try {
            initDatosCompartidos();
            FormContacto contacto = contactoService.getByID(id)
                    .orElse(new FormContacto());
            model.addAttribute("contacto", contacto);
            model.addAttribute("contactos", contactos);
            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el contacto: " + e.getMessage());
            return rutaHTML;
        }
    }

    // DELETE: Eliminar contacto
    @GetMapping("/delete/{id}")
    public String eliminarContacto(@PathVariable("id") Long id, Model model) {
        try {
            contactoService.deleteByID(id);
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            initDatosCompartidos();
            model.addAttribute("error", "Error al eliminar el contacto: " + e.getMessage());
            model.addAttribute("contactos", contactos);
            return rutaHTML;
        }
    }
}
