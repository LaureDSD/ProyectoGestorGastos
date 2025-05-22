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

@Controller
@RequestMapping("/admin/tickets")
public class TicketWebController {

    private final String rutaHTML = "/admin/tickets";

    @Autowired
    private TicketServiceImpl ticketService;

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

    // Listar todos los tickets
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

    // Formulario para editar un ticket
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

    // Guardar un ticket
    @PostMapping("/save")
    public String saveTicket(@ModelAttribute Ticket ticket, Model model) {
        try {
            // Obtener y establecer el usuario completo si hay un ID
            if (ticket.getUser() != null && ticket.getUser().getId() != null) {
                User fullUser = userService.getUserById(ticket.getUser().getId()).orElse(null);
                ticket.setUser(fullUser);
            }

            // Obtener y establecer la categoría completa si hay un ID
            if (ticket.getCategory() != null && ticket.getCategory().getId() != null) {
                CategoryExpense fullCategory = categoryService.getByID(ticket.getCategory().getId()).orElse(null);
                ticket.setCategory(fullCategory);
            }

            ticketService.setItem(ticket);
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            initDatosCompartidos();
            model.addAttribute("error", "Error al guardar el ticket: " + e.getMessage());
            return rutaHTML;
        }
    }


    // Eliminar ticket
    @GetMapping("/delete/{id}")
    public String eliminarTicket(@PathVariable("id") Long id, Model model) {
        try {
            return ticketService.getByID(id).map(ticket -> {
                ticketService.deleteByID(ticket.getSpentId());
                return "redirect:" + rutaHTML;
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
