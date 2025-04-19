package Proyecto.GestorAPI.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Proyecto.GestorAPI.models.User;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "AI Chat (Modulo simulado)", description = "Operaciones para gestionar el chat con IA")
public class AIChatController {

    @PostMapping("/message")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Enviar un mensaje al chat y recibir una respuesta por IA"
    )
    public ResponseEntity<String> sendMessage(@RequestBody String clientMessage, @RequestAttribute("user") User user) {


        if (user == null) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }


        String aiResponse = simulateAIResponse(clientMessage);

        return ResponseEntity.ok(aiResponse);
    }

    private String simulateAIResponse(String clientMessage) {
        return "Respuesta generada por IA: " + clientMessage;
    }
}
