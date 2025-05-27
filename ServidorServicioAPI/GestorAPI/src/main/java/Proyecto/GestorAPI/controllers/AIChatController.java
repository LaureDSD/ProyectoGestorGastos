package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.services.AIChatService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador REST para gestionar el chat con inteligencia artificial.
 * Solo accesible para usuarios autenticados mediante token Bearer.
 *
 * @author
 */
@RestController
@RequestMapping("/api/chat")
@Tag(name = "AI Chat (User Only)", description = "Operaciones para gestionar el chat con IA")
public class AIChatController {

    // Servicio para gestionar operaciones relacionadas con usuarios
    @Autowired
    private UserService userService;

    // Servicio encargado de la lógica del chat con inteligencia artificial
    @Autowired
    private AIChatService aiChatService;

    /**
     * Endpoint para enviar un mensaje al sistema de chat con IA.
     * Retorna la respuesta generada por la IA como texto plano.
     * Requiere autenticación con token Bearer.
     *
     * @param clientMessage Mensaje enviado por el cliente (en el cuerpo de la solicitud)
     * @param currentUser Usuario autenticado extraído del contexto de seguridad
     * @return Respuesta generada por la IA o un mensaje de error
     */
    @PostMapping("/message")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Enviar un mensaje al chat y recibir una respuesta por IA",
            description = "Este endpoint permite a un usuario autenticado enviar un mensaje a un modelo de IA y recibir una respuesta generada."
    )
    public ResponseEntity<String> sendMessage(
            @RequestBody String clientMessage,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        // Verifica si el usuario está autenticado
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Usuario no autenticado"); // 401 Unauthorized
        }

        try {
            // Procesa el mensaje con el servicio de IA y obtiene una respuesta
            String aiResponse = aiChatService.enviarMensaje(clientMessage);
            return ResponseEntity.ok(aiResponse); // 200 OK con la respuesta de la IA
        } catch (Exception e) {
            // Devuelve un error en caso de que falle el procesamiento
            return ResponseEntity.badRequest().body("Error al procesar solicitud: " + e.getMessage()); // 400 Bad Request
        }
    }
}
