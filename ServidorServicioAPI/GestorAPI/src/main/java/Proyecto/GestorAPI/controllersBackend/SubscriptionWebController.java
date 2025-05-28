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

/**
 * Controlador web para la gestión de suscripciones dentro del área administrativa.
 *
 * Define endpoints para listar, crear, editar y eliminar suscripciones.
 *
 * La clase maneja el mapeo de URLs bajo "/admin/subscriptions" y utiliza Thymeleaf
 * para la representación de vistas HTML.
 */
@Controller
@RequestMapping("/admin/subscriptions")
public class SubscriptionWebController {

    /**
     * Ruta común para las vistas HTML relacionadas con suscripciones.
     */
    private final String rutaHTML = "admin/subscriptions";

    @Autowired
    private SubscriptionServiceImpl subscriptionService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryExpenseServiceImpl categoryService;

    /**
     * Listado en memoria de usuarios disponibles, para carga en formularios.
     */
    private List<User> usuarios;

    /**
     * Listado en memoria de categorías de gasto, para carga en formularios.
     */
    private List<CategoryExpense> categorias;

    /**
     * Listado en memoria de tipos de gasto (enumeración ExpenseClass), para carga en formularios.
     */
    private List<ExpenseClass> tiposGasto;

    /**
     * Inicializa datos comunes que se comparten entre diferentes vistas:
     * usuarios, categorías y tipos de gasto.
     */
    private void initDatosCompartidos() {
        usuarios = userService.getUsers();
        categorias = categoryService.getAll();
        tiposGasto = List.of(ExpenseClass.values());
    }

    /**
     * Endpoint GET que lista todas las suscripciones.
     * Carga los datos necesarios y los añade al modelo para la vista.
     *
     * @param model Modelo para pasar atributos a la vista.
     * @return Nombre de la plantilla HTML para mostrar las suscripciones.
     */
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

    /**
     * Endpoint GET para mostrar el formulario de creación o edición de una suscripción.
     * Si se proporciona un id válido, carga la suscripción existente; si no, crea una nueva.
     *
     * @param id Identificador de la suscripción a editar.
     * @param model Modelo para pasar atributos a la vista.
     * @return Nombre de la plantilla HTML para crear o editar una suscripción.
     */
    @GetMapping("/edit/{id}")
    public String editarSubscription(@PathVariable("id") Long id, Model model) {
        try {
            initDatosCompartidos();
            Subscription subscription = (id != null)
                    ? subscriptionService.getByID(id).orElse(new Subscription())
                    : new Subscription();

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

    /**
     * Endpoint POST para guardar una suscripción, tanto para crear una nueva
     * como para actualizar una existente.
     *
     * @param subscription Objeto Subscription recibido desde el formulario.
     * @param model Modelo para pasar atributos a la vista.
     * @return Redirección a la lista de suscripciones o recarga del formulario en caso de error.
     */
    @PostMapping("/save")
    public String saveSubscription(@ModelAttribute Subscription subscription, Model model) {
        try {
            // Establece el usuario completo si el ID está presente
            if (subscription.getUser() != null && subscription.getUser().getId() != null) {
                User fullUser = userService.getUserById(subscription.getUser().getId()).orElse(null);
                subscription.setUser(fullUser);
            }

            // Establece la categoría completa si el ID está presente
            if (subscription.getCategory() != null && subscription.getCategory().getId() != null) {
                CategoryExpense fullCategory = categoryService.getByID(subscription.getCategory().getId()).orElse(null);
                subscription.setCategory(fullCategory);
            }

            subscription.setExpenseDate(subscription.getStart());
            System.out.println("Sub: "+subscription);
            subscriptionService.setItem(subscription);
            return "redirect:/" + rutaHTML;
        } catch (Exception e) {
            initDatosCompartidos();
            model.addAttribute("error", "Error al guardar la suscripción: " + e.getMessage());
            model.addAttribute("subscription", subscription);
            return rutaHTML;
        }
    }

    /**
     * Endpoint GET para eliminar una suscripción según su id.
     * En caso de éxito, redirige a la lista; si no, muestra error.
     *
     * @param id Identificador de la suscripción a eliminar.
     * @param model Modelo para pasar atributos a la vista.
     * @return Redirección a la lista de suscripciones o recarga con mensaje de error.
     */
    @GetMapping("/delete/{id}")
    public String eliminarSubscription(@PathVariable("id") Long id, Model model) {
        try {
            return subscriptionService.getByID(id).map(subscription -> {
                subscriptionService.deleteByID(subscription.getSpentId());
                return "redirect:/" + rutaHTML;
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
