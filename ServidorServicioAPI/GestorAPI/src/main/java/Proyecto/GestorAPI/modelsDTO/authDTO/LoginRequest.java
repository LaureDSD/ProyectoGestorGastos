package Proyecto.GestorAPI.modelsDTO.authDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Clase de solicitud para el inicio de sesión de un usuario.
 *
 * Este DTO (`LoginRequest`) representa los datos que el cliente debe enviar para autenticar a un usuario
 * en el sistema. Se utiliza para la validación y transferencia de información desde el cliente hacia el servidor.
 *
 * La clase es parte del flujo de autenticación en una API RESTful, donde el cliente envía las credenciales de inicio
 * de sesión para validar al usuario.
 */
public record LoginRequest(
        @Schema(example = "user")  // Esta anotación de Swagger proporciona un ejemplo de valor para la documentación de la API
        @NotBlank  // Valida que el campo no esté vacío
        String user,             // Nombre de usuario para el inicio de sesión o email

        @Schema(example = "user")   // Ejemplo de contraseña en la documentación de la API
        @NotBlank                    // El campo no debe estar vacío
        String password) {          // Contraseña del usuario para el inicio de sesión
}
