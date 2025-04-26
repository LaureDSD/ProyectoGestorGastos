package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.User;

/**
 * Clase de transferencia de datos (DTO) para la entidad `User`.
 *
 * El propósito de un DTO (Data Transfer Object) es simplificar el intercambio de datos entre capas de la
 * aplicación. En este caso, `UserDto` proporciona una representación simplificada de la entidad `User`,
 * que puede ser utilizada para la comunicación entre la capa de servicios y la capa de presentación, o entre
 * el cliente y el servidor en una API RESTful.
 */
public record UserDto(Long id, String username, String name, String email, String role) {

    /**
     * Método estático para convertir una entidad `User` en un DTO `UserDto`.
     *
     * Este método proporciona una forma de mapear una entidad `User` a su correspondiente DTO `UserDto`.
     * De esta forma, evitamos exponer directamente la entidad `User` con sus detalles internos y proporcionamos
     * una forma más segura y controlada de transferir datos.
     *
     * @param user La entidad `User` a convertir en un DTO.
     * @return El DTO `UserDto` con los datos del `User` proporcionado.
     */
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
