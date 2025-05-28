package Proyecto.GestorAPI.controllersBackend;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.servicesimpl.TicketServiceImpl;
import Proyecto.GestorAPI.servicesimpl.UserServiceImpl;
import Proyecto.GestorAPI.servicesimpl.CategoryExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador web para la gestión de tickets dentro del área administrativa.
 *
 * Provee funcionalidades para listar, editar, guardar y eliminar tickets.
 * Utiliza Thymeleaf para la representación de vistas HTML.
 *
 * Mapea las URLs bajo "/admin/tickets".
 */
@Controller
@RequestMapping("/admin/tickets")
public class TicketWebController {

    /**
     * Ruta común para las vistas HTML relacionadas con tickets.
     */
    private final String rutaHTML = "admin/tickets";

    @Autowired
    private TicketServiceImpl ticketService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryExpenseServiceImpl categoryService;

    /**
     * Listado de usuarios cargado para formularios.
     */
    private List<User> usuarios;

    /**
     * Listado de categorías de gasto cargado para formularios.
     */
    private List<CategoryExpense> categorias;

    /**
     * Listado de tipos de gasto (enumeración ExpenseClass) cargado para formularios.
     */
    private List<ExpenseClass> tiposGasto;

    /**
     * Inicializa datos compartidos entre vistas: usuarios, categorías y tipos de gasto.
     */
    private void initDatosCompartidos() {
        usuarios = userService.getUsers();
        categorias = categoryService.getAll();
        tiposGasto = List.of(ExpenseClass.values());
    }

    /**
     * Endpoint GET que muestra la lista completa de tickets.
     * Añade los datos necesarios al modelo para la vista.
     *
     * @param model Modelo para pasar atributos a la vista.
     * @return Nombre de la plantilla HTML para mostrar los tickets.
     */
    @GetMapping
    public String showTicketsList(Model model) {
        try {
            initDatosCompartidos();
            List<Ticket> tickets = ticketService.getAll();

            model.addAttribute("tickets", tickets);
            model.addAttribute("ticket", new Ticket());
            model.addAttribute("users", usuarios);
            model.addAttribute("categories", categorias);
            model.addAttribute("expenseTypes", tiposGasto);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los tickets: " + e.getMessage());
            return rutaHTML;
        }
    }

    /**
     * Endpoint GET para mostrar el formulario de edición de un ticket.
     * Carga el ticket por id, o crea uno nuevo si no se encuentra.
     *
     * @param id Identificador del ticket a editar.
     * @param model Modelo para pasar atributos a la vista.
     * @return Nombre de la plantilla HTML para editar un ticket.
     */
    @GetMapping("/edit/{id}")
    public String editarTicket(@PathVariable("id") Long id, Model model) {
        try {
            initDatosCompartidos();
            Ticket ticket = (id != null) ? ticketService.getByID(id).orElse(new Ticket()) : new Ticket();

            model.addAttribute("ticket", ticket);
            model.addAttribute("users", usuarios);
            model.addAttribute("categories", categorias);
            model.addAttribute("expenseTypes", tiposGasto);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el ticket para editar: " + e.getMessage());
            return rutaHTML;
        }
    }

    /**
     * Endpoint POST para guardar un ticket (creación o actualización).
     * Recupera objetos completos de usuario y categoría para persistencia correcta.
     *
     * @param ticket Objeto Ticket enviado desde el formulario.
     * @param model Modelo para pasar atributos a la vista.
     * @return Redirección a la lista de tickets o recarga con error.
     */
    @PostMapping("/save")
    public String saveTicket(@ModelAttribute Ticket ticket, Model model) {
        try {
            // Establece el usuario completo si el ID está presente
            if (ticket.getUser() != null && ticket.getUser().getId() != null) {
                User fullUser = userService.getUserById(ticket.getUser().getId()).orElse(null);
                ticket.setUser(fullUser);
            }

            // Establece la categoría completa si el ID está presente
            if (ticket.getCategory() != null && ticket.getCategory().getId() != null) {
                CategoryExpense fullCategory = categoryService.getByID(ticket.getCategory().getId()).orElse(null);
                ticket.setCategory(fullCategory);
            }
            System.out.println("Tick:"+ticket);
            ticketService.setItem(ticket);
            return "redirect:/" + rutaHTML;
        } catch (Exception e) {
            initDatosCompartidos();
            model.addAttribute("error", "Error al guardar el ticket: " + e.getMessage());
            return rutaHTML;
        }
    }

    /**
     * Endpoint GET para eliminar un ticket según su id.
     * Redirige a la lista si se elimina correctamente o muestra error si no se encuentra.
     *
     * @param id Identificador del ticket a eliminar.
     * @param model Modelo para pasar atributos a la vista.
     * @return Redirección a la lista de tickets o recarga con mensaje de error.
     */
    @GetMapping("/delete/{id}")
    public String eliminarTicket(@PathVariable("id") Long id, Model model) {
        try {
            return ticketService.getByID(id).map(ticket -> {
                ticketService.deleteByID(ticket.getSpentId());
                return "redirect:/" + rutaHTML;
            }).orElseGet(() -> {
                model.addAttribute("error", "Ticket no encontrado.");
                return rutaHTML;
            });
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el ticket: " + e.getMessage());
            return rutaHTML;
        }
    }
}
