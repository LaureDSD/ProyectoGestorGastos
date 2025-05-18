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

@RestController
@RequestMapping("/api/chat")
@Tag(name = "AI Chat (User Only)", description = "Operaciones para gestionar el chat con IA")
public class AIChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private AIChatService aiChatService;

    @PostMapping("/message")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Enviar un mensaje al chat y recibir una respuesta por IA"
    )
    public ResponseEntity<String> sendMessage(
            @RequestBody String clientMessage,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
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
