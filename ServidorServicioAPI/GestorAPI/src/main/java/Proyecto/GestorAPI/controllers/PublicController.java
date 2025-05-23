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
 * sobre la aplicación, como el número total de usuarios, gastos y formularios de contacto,
 * además de funciones para recuperación de contraseña.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
@Tag(name = "Endpoints Publicos (Open)", description = "Información adicional pública del servidor")
public class PublicController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final TicketService ticketService;

    @Autowired
    private final SpentService spentService;

    @Autowired
    private FormContactoService contactoService;

    @Autowired
    private AuthService authService;

    /**
     * Endpoint para obtener el número total de usuarios registrados en la aplicación.
     *
     * @return Cantidad total de usuarios almacenados en la base de datos.
     */
    @GetMapping("/usuarios")
    @Operation(summary = "Obtener número total de usuarios")
    public Integer getNumberOfUsers() {
        return userService.getCountUsers();
    }

    /**
     * Endpoint para obtener el número total de gastos registrados en la aplicación.
     *
     * @return Cantidad total de gastos almacenados en la base de datos.
     */
    @GetMapping("/gastos")
    @Operation(summary = "Obtener número total de gastos")
    public Integer getNumberOfSpennts() {
        return spentService.getCountSpents();
    }

    /**
     * Endpoint placeholder para obtener las suscripciones más populares.
     *
     * @return Nombre de la suscripción más popular (hardcodeado).
     */
    @GetMapping("/subscripcionesTop")
    @Operation(summary = "Obtener suscripciones más populares (placeholder)")
    public String getTopSubscription() {
        return "Tienda1";
    }

    /**
     * Endpoint placeholder para obtener las tiendas más visitadas.
     *
     * @return Nombre de la tienda más visitada (hardcodeado).
     */
    @GetMapping("/tiendasTop")
    @Operation(summary = "Obtener tiendas más visitadas (placeholder)")
    public String getTopShops() {
        return "Tienda2";
    }

    /**
     * Endpoint placeholder para obtener los productos más comprados.
     *
     * @return Nombre del producto más comprado (hardcodeado).
     */
    @GetMapping("/productosTop")
    @Operation(summary = "Obtener productos más comprados (placeholder)")
    public String getTopProducts() {
        return "Tempo";
    }

    /**
     * Endpoint para solicitar recuperación de contraseña vía email.
     *
     * Recibe una petición con el email, y si es válido envía un correo de recuperación.
     *
     * @param request Objeto que contiene el email para recuperación.
     * @return Mensaje de éxito o error en el procesamiento.
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar recuperación de contraseña")
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

    /**
     * Endpoint para guardar un formulario de contacto enviado públicamente.
     *
     * Recibe el formulario, establece el estado como no revisado y lo almacena.
     *
     * @param contacto Objeto FormContacto con los datos del formulario.
     * @return El formulario guardado o mensaje de error en caso de fallo.
     */
    @PostMapping("/contacto")
    @Operation(summary = "Guardar formulario de contacto")
    public ResponseEntity<?> guardar(@RequestBody FormContacto contacto) {
        try {
            contacto.setRevisado(false);
            return ResponseEntity.ok(contactoService.setItem(contacto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar solicitud: " + e.getMessage());
        }
    }

}
