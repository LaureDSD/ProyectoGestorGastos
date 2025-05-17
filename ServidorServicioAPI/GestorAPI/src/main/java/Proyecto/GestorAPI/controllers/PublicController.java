package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.services.SpentService;
import Proyecto.GestorAPI.services.TicketService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Controlador público que proporciona información general sobre la aplicación.
 *
 * Este controlador expone endpoints públicos que permiten obtener estadísticas generales
 * sobre la aplicación, como el número total de usuarios y tickets.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
@Tag(name = "Estadisticas Publicas (Public access)", description = "Informacion adicional publica del servidor")
public class PublicController {

    private final UserService userService;
    private final TicketService ticketService;
    private final SpentService spentService;

    /**
     * Endpoint para obtener el número total de usuarios registrados en la aplicación.
     *
     * Este endpoint retorna la cantidad total de usuarios almacenados en la base de datos.
     *
     * @return El número total de usuarios.
     */
    @GetMapping("/usuarios")
    public Integer getNumberOfUsers() {
        return userService.getUsers().size();
    }

    /**
     * Endpoint para obtener el número total de tickets registrados en la aplicación.
     *
     * Este endpoint retorna la cantidad total de tickets almacenados en la base de datos.
     *
     * @return El número total de tickets.
     */
    @GetMapping("/gastos")
    public Integer getNumberOfTickets() {
        return ticketService.getAll().size();
    }

    // Futuro: Endpoint para ver las suscripciones más populares,
    // tiendas más visitadas y productos más comprados.

    @GetMapping("/subscripcionesTop")
    public String getTopSubscription() {
        return "Tienda1";
    }

    @GetMapping("/tiendasTop")
    public String getTopShops() {
        return "Tienda2";
    }

    @GetMapping("/productosTop")
    public String getTopProducts() {
        return "Tempo";
    }

}
