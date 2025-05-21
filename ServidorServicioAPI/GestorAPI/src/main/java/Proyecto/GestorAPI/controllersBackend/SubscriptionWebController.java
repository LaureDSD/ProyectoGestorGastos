package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.Subscription;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.servicesimpl.SubscriptionServiceImpl;
import Proyecto.GestorAPI.servicesimpl.UserServiceImpl;
import Proyecto.GestorAPI.servicesimpl.CategoryExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/subscriptions")
public class SubscriptionWebController {

    private final String rutaHTML = "/admin/subscriptions";

    @Autowired
    private SubscriptionServiceImpl subscriptionService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryExpenseServiceImpl categoryService;

    // Listar todas las suscripciones
    @GetMapping
    public String listarSubscripciones(Model model) {
        try {
            List<Subscription> subscriptions = subscriptionService.getAll();
            List<User> users = userService.getUsers();
            List<CategoryExpense> categories = categoryService.getAll();
            List<ExpenseClass> expenseTypes = List.of(ExpenseClass.values());

            model.addAttribute("subscriptions", subscriptions);
            model.addAttribute("subscription", new Subscription());
            model.addAttribute("users", users);
            model.addAttribute("categories", categories);
            model.addAttribute("expenseTypes", expenseTypes);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar las suscripciones: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Formulario de edición
    @GetMapping("/edit/{id}")
    public String editarSubscripcion(@PathVariable("id") Long id, Model model) {
        try {
            Subscription subscription = subscriptionService.getByID(id).orElse(new Subscription());
            List<User> users = userService.getUsers();
            List<CategoryExpense> categories = categoryService.getAll();
            List<ExpenseClass> expenseTypes = List.of(ExpenseClass.values());

            model.addAttribute("subscription", subscription);
            model.addAttribute("users", users);
            model.addAttribute("categories", categories);
            model.addAttribute("expenseTypes", expenseTypes);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la suscripción para editar: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Guardar suscripción (crear/actualizar)
    @PostMapping("/save")
    public String guardarSubscripcion(@ModelAttribute Subscription subscription, Model model) {
        try {
            // Lógica específica para suscripciones
            if (subscription.getSpentId() == null) {
                // Nueva suscripción
                subscription.setCreatedAt(LocalDateTime.now());
                subscription.setUpdatedAt(LocalDateTime.now());
                // Calcular acumulado inicial si es necesario
                subscription.setAccumulate(subscription.getTotal());
            } else {
                // Actualización de suscripción
                subscription.setUpdatedAt(LocalDateTime.now());
                // Aquí podrías añadir lógica para recalcular el acumulado si cambian fechas o intervalo
            }

            subscriptionService.setItem(subscription);
            return "redirect:/admin/subscriptions";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar la suscripción: " + e.getMessage());

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

    // Eliminar suscripción
    @GetMapping("/delete/{id}")
    public String eliminarSubscripcion(@PathVariable("id") Long id, Model model) {
        try {
            subscriptionService.deleteByID(subscriptionService.getByID(id).orElse(null).getSpentId());
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar la suscripción: " + e.getMessage());
            return rutaHTML;
        }
    }

}