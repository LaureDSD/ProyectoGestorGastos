package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador web para la gestión de intentos de inicio de sesión.
 *
 * <p>
 * Proporciona funcionalidad para listar los intentos de login registrados,
 * mostrando información relevante para administración y auditoría.
 * </p>
 *
 * <p>
 * La ruta base para este controlador es <code>/admin/login-attempts</code>.
 * </p>
 */
@Controller
@RequestMapping("/admin/login-attempts")
public class LoginAttemptWebController {

    /**
     * Ruta base para la plantilla HTML correspondiente a la gestión
     * de intentos de login.
     */
    private final String rutaHTML = "/admin/login-attempts";

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    /**
     * Método reservado para inicialización de datos compartidos para la vista.
     * Actualmente no realiza ninguna acción, pero preparado para futuras necesidades.
     */
    private void initDatosCompartidos() {
        // Método preparado para futuras implementaciones.
    }

    /**
     * Endpoint GET para listar todos los intentos de inicio de sesión.
     *
     * <p>
     * Consulta al servicio para obtener todos los intentos registrados,
     * los añade al modelo bajo el atributo "attempts" y retorna la vista.
     * </p>
     *
     * <p>
     * En caso de error, se añade un mensaje descriptivo al modelo y se
     * retorna igualmente la vista, permitiendo mostrar el error.
     * </p>
     *
     * @param model objeto {@link Model} para pasar atributos a la vista.
     * @return nombre de la plantilla Thymeleaf para mostrar intentos de login.
     */
    @GetMapping
    public String listarIntentosLogin(Model model) {
        initDatosCompartidos(); // Para futuro uso

        try {
            List<LoginAttempt> intentos = loginAttemptService.getAll();
            model.addAttribute("attempts", intentos);
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los intentos de login: " + e.getMessage());
        }

        return rutaHTML;
    }
}
