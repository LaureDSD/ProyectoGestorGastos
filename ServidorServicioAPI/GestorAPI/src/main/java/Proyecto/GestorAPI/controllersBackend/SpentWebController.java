package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.Spent;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.servicesimpl.SpentServiceImpl;
import Proyecto.GestorAPI.servicesimpl.UserServiceImpl;
import Proyecto.GestorAPI.servicesimpl.CategoryExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador web para la gestión de gastos (expenses).
 *
 * <p>
 * Proporciona endpoints para listar, editar, guardar y eliminar gastos,
 * junto con datos auxiliares como usuarios, categorías y tipos de gasto.
 * </p>
 *
 * <p>
 * La ruta base para este controlador es <code>/admin/expenses</code>.
 * </p>
 */
@Controller
@RequestMapping("/admin/expenses")
public class SpentWebController {

    /**
     * Ruta base para la plantilla HTML correspondiente a la gestión de gastos.
     */
    private final String rutaHTML = "/admin/expenses";

    @Autowired
    private SpentServiceImpl spentService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryExpenseServiceImpl categoryService;

    private List<User> usuarios;
    private List<CategoryExpense> categorias;
    private List<ExpenseClass> tiposGasto;

    /**
     * Inicializa los datos compartidos necesarios para los formularios y vistas,
     * como lista de usuarios, categorías y tipos de gasto.
     */
    private void initDatosCompartidos() {
        usuarios = userService.getUsers();
        categorias = categoryService.getAll();
        tiposGasto = List.of(ExpenseClass.values());
    }

    /**
     * Endpoint GET para listar todos los gastos registrados.
     *
     * <p>
     * Añade al modelo la lista de gastos, un objeto gasto vacío para el formulario,
     * y los datos auxiliares (usuarios, categorías, tipos de gasto).
     * </p>
     *
     * @param model Modelo para pasar atributos a la vista.
     * @return Nombre de la plantilla Thymeleaf para mostrar gastos.
     */
    @GetMapping
    public String listarGastos(Model model) {
        try {
            initDatosCompartidos();
            List<Spent> gastos = spentService.getAll();

            model.addAttribute("expenses", gastos);
            model.addAttribute("expense", new Spent());
            model.addAttribute("users", usuarios);
            model.addAttribute("categories", categorias);
            model.addAttribute("expenseTypes", tiposGasto);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los gastos: " + e.getMessage());
            return rutaHTML;
        }
    }

    /**
     * Endpoint GET para cargar el formulario de edición de un gasto específico.
     *
     * <p>
     * Obtiene el gasto por ID o crea uno nuevo si el ID es nulo o no existe,
     * y añade los datos auxiliares para el formulario.
     * </p>
     *
     * @param id ID del gasto a editar.
     * @param model Modelo para pasar atributos a la vista.
     * @return Nombre de la plantilla Thymeleaf para editar gasto.
     */
    @GetMapping("/edit/{id}")
    public String editarGasto(@PathVariable("id") Long id, Model model) {
        try {
            initDatosCompartidos();
            Spent gasto = (id != null) ? spentService.getByID(id).orElse(new Spent()) : new Spent();

            model.addAttribute("expense", gasto);
            model.addAttribute("users", usuarios);
            model.addAttribute("categories", categorias);
            model.addAttribute("expenseTypes", tiposGasto);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el gasto para editar: " + e.getMessage());
            return rutaHTML;
        }
    }

    /**
     * Endpoint POST para guardar un gasto, ya sea nuevo o actualizado.
     *
     * <p>
     * Realiza la asociación completa del usuario y categoría si sus IDs están presentes,
     * luego delega el guardado al servicio correspondiente.
     * </p>
     *
     * @param gasto Objeto {@link Spent} que contiene los datos del gasto a guardar.
     * @param model Modelo para pasar atributos a la vista en caso de error.
     * @return Redirección a la lista de gastos si es exitoso, o la vista con error.
     */
    @PostMapping("/save")
    public String guardarGasto(@ModelAttribute Spent gasto, Model model) {
        try {
            // Obtener y establecer el usuario completo si hay un ID
            if (gasto.getUser() != null && gasto.getUser().getId() != null) {
                User fullUser = userService.getUserById(gasto.getUser().getId()).orElse(null);
                gasto.setUser(fullUser);
            }

            // Obtener y establecer la categoría completa si hay un ID
            if (gasto.getCategory() != null && gasto.getCategory().getId() != null) {
                CategoryExpense fullCategory = categoryService.getByID(gasto.getCategory().getId()).orElse(null);
                gasto.setCategory(fullCategory);
            }
            spentService.setItem(gasto);
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            initDatosCompartidos();
            model.addAttribute("error", "Error al guardar el gasto: " + e.getMessage());
            model.addAttribute("expense", gasto);
            return rutaHTML;
        }
    }

    /**
     * Endpoint GET para eliminar un gasto por su ID.
     *
     * <p>
     * Verifica que el gasto exista antes de eliminarlo. Si no se encuentra,
     * se añade un mensaje de error al modelo.
     * </p>
     *
     * @param id ID del gasto a eliminar.
     * @param model Modelo para pasar atributos a la vista en caso de error.
     * @return Redirección a la lista de gastos o vista con error.
     */
    @GetMapping("/delete/{id}")
    public String eliminarGasto(@PathVariable("id") Long id, Model model) {
        try {
            return spentService.getByID(id).map(gasto -> {
                spentService.deleteByID(gasto.getSpentId());
                return "redirect:" + rutaHTML;
            }).orElseGet(() -> {
                model.addAttribute("error", "Gasto no encontrado.");
                return rutaHTML;
            });
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el gasto: " + e.getMessage());
            return rutaHTML;
        }
    }
}
