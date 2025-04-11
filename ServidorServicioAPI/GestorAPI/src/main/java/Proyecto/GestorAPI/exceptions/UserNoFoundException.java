package Proyecto.GestorAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNoFoundException extends RuntimeException {
    public UserNoFoundException(String message) {
        super(message);
    }
}
