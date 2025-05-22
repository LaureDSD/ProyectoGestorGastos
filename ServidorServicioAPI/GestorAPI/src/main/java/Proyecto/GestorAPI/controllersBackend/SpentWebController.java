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

    private List<User> usuarios;
    private List<CategoryExpense> categorias;
    private List<ExpenseClass> tiposGasto;

    private void initDatosCompartidos() {
        usuarios = userService.getUsers();
        categorias = categoryService.getAll();
        tiposGasto = List.of(ExpenseClass.values());
    }

    // Listar todos los gastos
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

    // Formulario de edición
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

    // Guardar gasto
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

    // Eliminar gasto
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
