package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.services.AIChatService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody ;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador REST para gestionar la comunicación con la IA vía chat.
 *
 * Este endpoint está restringido a usuarios autenticados.
 */
@RestController
@RequestMapping("/api/chat")
@Tag(name = "AI Chat (User Only)", description = "Operaciones para gestionar el chat con IA")
public class AIChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private AIChatService aiChatService;

    /**
     * Enviar un mensaje al asistente de IA y recibir una respuesta generada.
     *
     * Requiere autenticación mediante token Bearer.
     *
     * @param clientMessage Mensaje en texto plano enviado por el usuario.
     * @param currentUser Usuario autenticado, inyectado automáticamente por Spring Security.
     * @return Respuesta generada por la IA.
     */
    @PostMapping("/message")
    @Operation(
            summary = "Enviar un mensaje al chat y recibir una respuesta por IA",
            description = "Este endpoint permite a un usuario autenticado enviar un mensaje de texto a un modelo de IA y recibir una respuesta generada.",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta generada correctamente",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Error al procesar el mensaje"),
            @ApiResponse(responseCode = "401", description = "No autenticado - el token es inválido o no fue proporcionado")
    })
    public ResponseEntity<String> sendMessage(
            @RequestBody(
                    description = "Mensaje en texto plano enviado por el usuario",
                    required = true,
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Hola, ¿en qué puedes ayudarme?"))
            ) String clientMessage,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }
        try {
            String aiResponse = aiChatService.enviarMensaje(clientMessage);
            return ResponseEntity.ok(aiResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar solicitud: " + e.getMessage());
        }
    }
}
