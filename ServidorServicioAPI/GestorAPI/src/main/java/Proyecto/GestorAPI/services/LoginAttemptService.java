package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.LoginAttempt;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Interfaz para el servicio que gestiona los intentos de inicio de sesión.
 * Proporciona métodos para registrar intentos, obtener registros y administrar entidades LoginAttempt.
 */
public interface LoginAttemptService {

    /**
     * Registra un intento de inicio de sesión para un usuario.
     * Marca si el intento fue exitoso o no.
     * La operación se ejecuta en una transacción.
     *
     * @param username Nombre de usuario del intento de login.
     * @param success  Indica si el intento fue exitoso (true) o fallido (false).
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    @Transactional
    boolean registerLoginAttempt(String username, boolean success);

    /**
     * Obtiene todos los registros de intentos de inicio de sesión.
     *
     * @return Lista con todos los objetos LoginAttempt registrados.
     */
    List<LoginAttempt> getAll();

    /**
     * Obtiene todos los intentos de inicio de sesión para un usuario específico,
     * identificado por su nombre de usuario o correo electrónico.
     *
     * @param username Nombre de usuario o email a buscar.
     * @return Lista con los LoginAttempt correspondientes.
     */
    List<LoginAttempt> getByUsernameOrEamil(String username);

    /**
     * Guarda o actualiza un registro de intento de inicio de sesión.
     *
     * @param loginAttempt Objeto LoginAttempt a guardar o actualizar.
     */
    void setItem(LoginAttempt loginAttempt);
}
