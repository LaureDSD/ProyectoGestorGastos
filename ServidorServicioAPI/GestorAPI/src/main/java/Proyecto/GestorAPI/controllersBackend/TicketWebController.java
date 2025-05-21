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

import java.time.LocalDateTime;
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

    // Listar todos los tickets
    @GetMapping
    public String listarTickets(Model model) {
        try {
            List<Ticket> tickets = ticketService.getAll();
            List<User> users = userService.getUsers();
            List<CategoryExpense> categories = categoryService.getAll();
            List<ExpenseClass> expenseTypes = List.of(ExpenseClass.values());

            model.addAttribute("tickets", tickets);
            model.addAttribute("ticket", new Ticket());
            model.addAttribute("users", users);
            model.addAttribute("categories", categories);
            model.addAttribute("expenseTypes", expenseTypes);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los tickets: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Formulario de edición
    @GetMapping("/edit/{id}")
    public String editarTicket(@PathVariable("id") Long id, Model model) {
        try {
            Ticket ticket = ticketService.getByID(id).orElse(new Ticket());
            List<User> users = userService.getUsers();
            List<CategoryExpense> categories = categoryService.getAll();
            List<ExpenseClass> expenseTypes = List.of(ExpenseClass.values());

            model.addAttribute("ticket", ticket);
            model.addAttribute("users", users);
            model.addAttribute("categories", categories);
            model.addAttribute("expenseTypes", expenseTypes);

            return rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el ticket para editar: " + e.getMessage());
            return rutaHTML;
        }
    }

    // Guardar ticket (crear/actualizar)
    @PostMapping("/save")
    public String guardarTicket(@ModelAttribute Ticket ticket, Model model) {
        try {
            // Establecer fechas si es nuevo
            if (ticket.getSpentId() == null) {
                ticket.setCreatedAt(LocalDateTime.now());
            }
            ticket.setUpdatedAt(LocalDateTime.now());

            ticketService.setItem(ticket);
            return "redirect:/admin/tickets";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el ticket: " + e.getMessage());

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

    // Eliminar ticket
    @GetMapping("/delete/{id}")
    public String eliminarTicket(@PathVariable("id") Long id, Model model) {
        try {
            ticketService.deleteByID(ticketService.getByID(id).orElse(null).getSpentId());
            return "redirect:" + rutaHTML;
        } catch (Exception e) {
            model.addAttribute("error", "Error al eliminar el ticket: " + e.getMessage());
            return rutaHTML;
        }
    }
}