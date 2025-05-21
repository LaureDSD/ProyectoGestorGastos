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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/expenses")
public class SpentWebController {

    private final String rutaHTML = "/admin/expenses";

    @Autowired
    private SpentServiceImpl spentService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryExpenseServiceImpl categoryService;

    // Listar todos los gastos genéricos (no incluye subclases)
    @GetMapping
    public String listarGastos(Model model) {
        try {
            List<Spent> gastos = spentService.getAll();
            List<User> users = userService.getUsers();
            List<CategoryExpense> categories = categoryService.getAll();
            List<ExpenseClass> expenseTypes = List.of(ExpenseClass.values());

            model.addAttribute("expenses", gastos);
            model.addAttribute("expense", new Spent());
            model.addAttribute("users", users);
            model.addAttribute("categories", categories);
            model.addAttribute("expenseTypes", expenseTypes);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los gastos: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Formulario de edición
    @GetMapping("/edit/{id}")
    public String editarGasto(@PathVariable("id") Long id, Model model) {
        try {
            Spent gasto = spentService.getByID(id).orElse(new Spent());
            List<User> users = userService.getUsers();
            List<CategoryExpense> categories = categoryService.getAll();
            List<ExpenseClass> expenseTypes = List.of(ExpenseClass.values());

            model.addAttribute("expense", gasto);
            model.addAttribute("users", users);
            model.addAttribute("categories", categories);
            model.addAttribute("expenseTypes", expenseTypes);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el gasto para editar: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Guardar gasto (crear/actualizar)
    @PostMapping("/save")
    public String guardarGasto(@ModelAttribute Spent gasto, Model model) {
        try {
            // Asegurar que el tipo sea genérico (para evitar confusión con subclases)
            gasto.setTypeExpense(ExpenseClass.GASTO_GENERICO);

            // Las fechas se manejan automáticamente con los callbacks @PrePersist y @PreUpdate
            spentService.setItem(gasto);
            return "redirect:/admin/expenses";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el gasto: " + e.getMessage());

            // Recargar datos necesarios para la vista en caso de error
            List<User> users = userService.getUsers();
            List<CategoryExpense> categories = categoryService.getAll();
            List<ExpenseClass> expenseTypes = List.of(ExpenseClass.values());

            model.addAttribute("users", users);
            model.addAttribute("categories", categories);
            model.addAttribute("expenseTypes", expenseTypes);

            return rutaHTML;
        }
    }

    // Eliminar gasto
    @GetMapping("/delete/{id}")
    public String eliminarGasto(@PathVariable("id") Long id, Model model) {
        try {
            spentService.deleteByID(spentService.getByID(id).orElse(null).getSpentId());
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el gasto: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Endpoint para ver detalles (incluyendo subclases si existen)
    @GetMapping("/view/{id}")
    public String verDetalleGasto(@PathVariable("id") Long id, Model model) {
        try {
            Spent gasto = spentService.getByID(id).orElse(null);
            if (gasto == null) {
                return "redirect:" + rutaHTML;
            }

            model.addAttribute("expense", gasto);

            // Determinar la vista específica según el tipo de gasto
            switch(gasto.getTypeExpense()) {
                case TICKET:
                    return "redirect:/admin/tickets/edit/" + id;
                case SUBSCRIPCION:
                    return "redirect:/admin/subscriptions/edit/" + id;
                default:
                    model.addAttribute("isGeneric", true);
                    return rutaHTML + "-detail";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el detalle del gasto: " + e.getMessage());
            return rutaHTML;
        }
    }
}