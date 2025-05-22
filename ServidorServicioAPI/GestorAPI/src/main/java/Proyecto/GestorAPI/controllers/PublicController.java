package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.FormContacto;
import Proyecto.GestorAPI.modelsDTO.ForgotPasswordRequest;
import Proyecto.GestorAPI.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador público que proporciona información general sobre la aplicación.
 *
 * Este controlador expone endpoints públicos que permiten obtener estadísticas generales
 * sobre la aplicación, como el número total de usuarios y tickets.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
@Tag(name = "Endpoints Publicos (Open)", description = "Informacion adicional publica del servidor")
public class PublicController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final TicketService ticketService;

    @Autowired
    private final SpentService spentService;

    @Autowired
    private ContactoService contactoService;


    @Autowired
    private AuthService authService;

    /**
     * Endpoint para obtener el número total de usuarios registrados en la aplicación.
     *
     * Este endpoint retorna la cantidad total de usuarios almacenados en la base de datos.
     *
     * @return El número total de usuarios.
     */
    @GetMapping("/usuarios")
    public Integer getNumberOfUsers() {
        return userService.getCountUsers();
    }

    /**
     * Endpoint para obtener el número total de gastos registrados en la aplicación.
     *
     * Este endpoint retorna la cantidad total de gastos almacenados en la base de datos.
     *
     * @return El número total de gastos.
     */
    @GetMapping("/gastos")
    public Integer getNumberOfSpennts() {
        return spentService.getCountSpents();
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

    @PostMapping("/forgot-password")
    @Operation(summary = "Pedir recuperacion, analogico")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            authService.processForgotPassword(request.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Correo de recuperación enviado si el email es válido.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar solicitud: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/contacto")
    @Operation(
            summary = "Guardar formulario"
    )
    public ResponseEntity<?> guardar(@RequestBody FormContacto contacto) {
        try {
        contacto.setRevisado(false);
        return ResponseEntity.ok(contactoService.setItem(contacto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar solicitud: " + e.getMessage());
        }
    }

}
