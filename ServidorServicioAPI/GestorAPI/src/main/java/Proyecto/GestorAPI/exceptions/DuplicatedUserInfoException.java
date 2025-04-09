package Proyecto.GestorAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para manejar casos en los que se detecta información duplicada de usuario
 * (por ejemplo, cuando un usuario intenta registrarse con un correo electrónico ya existente).
 * Esta excepción devolverá una respuesta HTTP con el estado de "CONFLICT" (409).
 */
@ResponseStatus(HttpStatus.CONFLICT) // Indica que la excepción provocará un estado de conflicto (HTTP 409)
public class DuplicatedUserInfoException extends RuntimeException {

    /**
     * Constructor que permite crear una instancia de la excepción con un mensaje personalizado.
     *
     * @param message El mensaje que describe el motivo de la excepción (por ejemplo, "El correo electrónico ya está registrado").
     */
    public DuplicatedUserInfoException(String message) {
        super(message); // Llama al constructor de la clase RuntimeException para pasar el mensaje de error.
    }
}
