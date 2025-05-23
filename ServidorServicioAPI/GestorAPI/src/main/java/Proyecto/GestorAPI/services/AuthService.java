package Proyecto.GestorAPI.services;

/**
 * Interfaz para el servicio de autenticación relacionado con la gestión de recuperación de contraseñas.
 * Define el contrato para procesar solicitudes de restablecimiento de contraseña mediante email.
 */
public interface AuthService {

    /**
     * Procesa la solicitud de recuperación de contraseña para el email proporcionado.
     *
     * @param email Dirección de correo electrónico del usuario que solicita la recuperación de contraseña.
     * @throws Exception Si ocurre un error durante el proceso de recuperación.
     */
    void processForgotPassword(String email) throws Exception;
}
