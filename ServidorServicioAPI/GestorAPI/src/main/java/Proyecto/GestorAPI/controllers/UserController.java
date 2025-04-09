package Proyecto.GestorAPI.controllers;


import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.UserDto;
import Proyecto.GestorAPI.security.CustomUserDetails;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los usuarios.
 * Permite obtener información del usuario actual, obtener la lista de usuarios,
 * obtener información de un usuario específico y eliminar un usuario.
 * Todas las operaciones están protegidas mediante autenticación y autorización basada en JWT.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Servicio que gestiona las operaciones de usuarios.
    private final UserService userService;

    /**
     * Obtiene la información del usuario actualmente autenticado.
     * Utiliza el objeto `CustomUserDetails` para obtener el usuario autenticado.
     *
     * @param currentUser El usuario actualmente autenticado.
     * @return Un objeto `UserDto` con la información del usuario autenticado.
     */
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        // Valida y obtiene el usuario por su nombre de usuario (username).
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        return UserDto.from(user); // Convierte el usuario a un DTO para retornar la información.
    }

    /**
     * Obtiene la lista de todos los usuarios.
     * Solo accesible por usuarios autenticados con el token Bearer.
     *
     * @return Una lista de objetos `UserDto` que representan la información de los usuarios.
     */
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public List<UserDto> getUsers() {
        // Obtiene todos los usuarios y los convierte a `UserDto` para ser enviados como respuesta.
        return userService.getUsers().stream()
                .map(UserDto::from) // Mapea cada usuario a un DTO.
                .collect(Collectors.toList()); // Recolecta los DTOs en una lista.
    }

    /**
     * Obtiene la información de un usuario específico por su nombre de usuario.
     * Solo accesible por usuarios autenticados con el token Bearer.
     *
     * @param username El nombre de usuario del cual se desea obtener la información.
     * @return Un objeto `UserDto` con la información del usuario.
     */
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable String username) {
        // Valida y obtiene el usuario por su nombre de usuario (username).
        return UserDto.from(userService.validateAndGetUserByUsername(username));
    }

    /**
     * Elimina un usuario específico por su nombre de usuario.
     * Solo accesible por usuarios autenticados con el token Bearer.
     *
     * @param username El nombre de usuario del cual se desea eliminar.
     * @return El DTO del usuario eliminado.
     */
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{username}")
    public UserDto deleteUser(@PathVariable String username) {
        // Valida y obtiene el usuario por su nombre de usuario (username).
        User user = userService.validateAndGetUserByUsername(username);
        userService.deleteUser(user); // Elimina el usuario de la base de datos.
        return UserDto.from(user); // Retorna el DTO del usuario eliminado.
    }
}
