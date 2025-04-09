package Proyecto.GestorAPI.modelsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Clase de solicitud para el registro de un nuevo usuario.
 *
 * Este DTO (`SignUpRequest`) representa los datos que el cliente debe enviar para registrar un nuevo usuario
 * en el sistema. Se utiliza para la validación y transferencia de información desde el cliente hacia el servidor.
 *
 * La clase está diseñada para ser utilizada en una API RESTful donde el cliente enviará los datos para crear una nueva cuenta.
 */
public record SignUpRequest(
        @Schema(example = "user1")  // Esta anotación de Swagger proporciona un ejemplo de valor para la documentación de la API
        @NotBlank  // Valida que el campo no esté vacío
        String username,             // Nombre de usuario

        @Schema(example = "user1")   // Este ejemplo se utiliza para la documentación de la API
        @NotBlank                    // El campo no debe estar vacío
        String password,             // Contraseña del usuario

        @Schema(example = "User1")   // Ejemplo del nombre en la documentación de la API
        @NotBlank                    // El nombre no puede estar vacío
        String name,                 // Nombre completo del usuario

        @Schema(example = "user1@gesthor.com")  // Ejemplo de correo electrónico para la documentación
        @Email                        // Valida que el valor sea un correo electrónico válido
        String email) {              // Correo electrónico del usuario
}
