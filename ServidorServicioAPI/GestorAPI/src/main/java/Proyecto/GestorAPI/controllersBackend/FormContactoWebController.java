package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.FormContacto;
import Proyecto.GestorAPI.servicesimpl.ContactoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador web para la gestión del formulario de contacto.
 *
 * <p>
 * Proporciona funcionalidades para listar, crear, editar y eliminar registros
 * de contacto enviados a través del formulario, utilizando vistas Thymeleaf.
 * </p>
 *
 * <p>
 * La ruta base para este controlador es <code>/admin/formcontacto</code>.
 * </p>
 *
 * Funcionalidades principales:
 * <ul>
 *   <li>Listar todos los registros de contacto</li>
 *   <li>Crear o actualizar un registro de contacto</li>
 *   <li>Editar un registro existente (precargar formulario)</li>
 *   <li>Eliminar un registro de contacto por su ID</li>
 * </ul>
 */
@Controller
@RequestMapping("/admin/formcontacto")
public class FormContactoWebController {

    /**
     * Ruta base para las vistas HTML relacionadas con el formulario de contacto.
     */
    private final String rutaHTML = "/admin/formcontacto";

    @Autowired
    private ContactoServiceImpl contactoService;

    /**
     * Lista temporal para almacenar los registros de contacto obtenidos del servicio.
     */
    private List<FormContacto> contactos;

    /**
     * Método privado que inicializa los datos compartidos necesarios para las vistas,
     * principalmente la lista de registros de contacto.
     */
    private void initDatosCompartidos() {
        contactos = contactoService.getAll();
    }

    /**
     * Endpoint GET para listar todos los registros de contacto.
     *
     * <p>
     * Carga la lista de contactos, añade un nuevo objeto vacío para formulario,
     * y retorna la vista correspondiente.
     * </p>
     *
     * En caso de error, añade un mensaje de error al modelo y retorna la misma vista.
     *
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return nombre de la plantilla Thymeleaf para listar contactos.
     */
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

    /**
     * Endpoint POST para crear o actualizar un registro de contacto.
     *
     * <p>
     * Recibe un objeto {@link FormContacto} desde el formulario, lo guarda o actualiza
     * mediante el servicio y redirige a la lista de contactos.
     * </p>
     *
     * En caso de error, recarga los datos y retorna la vista con mensaje de error y
     * formulario prellenado con los datos ingresados.
     *
     * @param contacto objeto {@link FormContacto} con los datos del formulario.
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return redirección a la lista o la misma vista en caso de error.
     */
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

    /**
     * Endpoint GET para mostrar el formulario de edición de un contacto.
     *
     * <p>
     * Busca el contacto por su ID. Si existe, lo agrega al modelo para su edición.
     * También añade la lista completa de contactos.
     * </p>
     *
     * En caso de error, añade un mensaje de error y retorna la vista de listado.
     *
     * @param id identificador único del contacto a editar.
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return nombre de la plantilla Thymeleaf para edición/listado de contactos.
     */
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

    /**
     * Endpoint GET para eliminar un contacto por su ID.
     *
     * <p>
     * Invoca el servicio para borrar el registro y redirige a la lista de contactos.
     * En caso de error, añade un mensaje de error y recarga la vista con datos.
     * </p>
     *
     * @param id identificador único del contacto a eliminar.
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return redirección a la lista de contactos o misma vista con error.
     */
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
