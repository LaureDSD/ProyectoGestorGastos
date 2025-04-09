package Proyecto.GestorAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para manejar casos en los que no se encuentra un ticket en la base de datos.
 * Esta excepción devolverá una respuesta HTTP con el estado "NOT_FOUND" (404).
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Indica que la excepción provocará un estado de "No Encontrado" (HTTP 404)
public class TicketNotFoundException extends RuntimeException {

    /**
     * Constructor que permite crear una instancia de la excepción con un mensaje personalizado.
     *
     * @param message El mensaje que describe el motivo de la excepción (por ejemplo, "Ticket no encontrado").
     */
    public TicketNotFoundException(String message) {
        super(message); // Llama al constructor de la clase RuntimeException para pasar el mensaje de error.
    }
}
