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

    private List<User> usuarios;
    private List<CategoryExpense> categorias;
    private List<ExpenseClass> tiposGasto;

    private void initDatosCompartidos() {
        usuarios = userService.getUsers();
        categorias = categoryService.getAll();
        tiposGasto = List.of(ExpenseClass.values());
    }

    // Listar todas las suscripciones
    @GetMapping
    public String showSubscriptionsList(Model model) {
        try {
            initDatosCompartidos();
            List<Subscription> subscriptions = subscriptionService.getAll();

            model.addAttribute("subscriptions", subscriptions);
            model.addAttribute("subscription", new Subscription());
            model.addAttribute("users", usuarios);
            model.addAttribute("categories", categorias);
            model.addAttribute("expenseTypes", tiposGasto);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar las suscripciones: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Formulario para crear o editar una suscripción
    @GetMapping("/edit/{id}")
    public String editarSubscription(@PathVariable("id") Long id, Model model) {
        try {
            initDatosCompartidos();
            Subscription subscription = (id != null) ? subscriptionService.getByID(id).orElse(new Subscription()) : new Subscription();

            model.addAttribute("subscription", subscription);
            model.addAttribute("users", usuarios);
            model.addAttribute("categories", categorias);
            model.addAttribute("expenseTypes", tiposGasto);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar la suscripción para editar: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Guardar una suscripción (creación o actualización)
    @PostMapping("/save")
    public String saveSubscription(@ModelAttribute Subscription subscription, Model model) {
        try {
            // Obtener y establecer el usuario completo si hay un ID
            if (subscription.getUser() != null && subscription.getUser().getId() != null) {
                User fullUser = userService.getUserById(subscription.getUser().getId()).orElse(null);
                subscription.setUser(fullUser);
            }

            // Obtener y establecer la categoría completa si hay un ID
            if (subscription.getCategory() != null && subscription.getCategory().getId() != null) {
                CategoryExpense fullCategory = categoryService.getByID(subscription.getCategory().getId()).orElse(null);
                subscription.setCategory(fullCategory);
            }

            subscriptionService.setItem(subscription);
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            initDatosCompartidos();
            model.addAttribute("error", "Error al guardar la suscripción: " + e.getMessage());
            model.addAttribute("subscription", subscription);
            return rutaHTML;
        }
    }

    // Eliminar suscripción
    @GetMapping("/delete/{id}")
    public String eliminarSubscription(@PathVariable("id") Long id, Model model) {
        try {
            return subscriptionService.getByID(id).map(subscription -> {
                subscriptionService.deleteByID(subscription.getSpentId());
                return "redirect:" + rutaHTML;
            }).orElseGet(() -> {
                model.addAttribute("error", "Suscripción no encontrada.");
                return rutaHTML;
            });
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar la suscripción: " + e.getMessage());
            return rutaHTML;
        }
    }
}
