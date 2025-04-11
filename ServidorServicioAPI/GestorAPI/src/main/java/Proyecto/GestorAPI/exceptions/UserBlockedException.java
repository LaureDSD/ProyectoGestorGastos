package Proyecto.GestorAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class UserBlockedException extends RuntimeException {
  public UserBlockedException(String message) {
    super(message);
  }
}