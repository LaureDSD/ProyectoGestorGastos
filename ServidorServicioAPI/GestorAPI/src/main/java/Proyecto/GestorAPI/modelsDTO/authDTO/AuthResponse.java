package Proyecto.GestorAPI.modelsDTO.authDTO;

/**
 * Clase de respuesta de autenticación.
 *
 * Este DTO (`AuthResponse`) representa la respuesta que se envía al cliente después de un intento de autenticación
 * exitoso. Contiene el token de acceso (`accessToken`) que el cliente utilizará para autenticar futuras solicitudes
 * al servidor.
 *
 * El `accessToken` es un JSON Web Token (JWT) o cualquier otro tipo de token que sirva para mantener la sesión activa
 * y garantizar que las solicitudes que se realicen al servidor provengan de un usuario autenticado.
 */
public record AuthResponse(String token) {
}
