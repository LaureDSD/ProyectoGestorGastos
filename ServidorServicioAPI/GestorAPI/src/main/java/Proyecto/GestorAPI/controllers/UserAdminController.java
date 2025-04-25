package Proyecto.GestorAPI.controllers;


import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.UserDto;
import Proyecto.GestorAPI.security.CustomUserDetails;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = "User Management (Admin only) ", description = "Gestion de usuarios")
public class UserAdminController {

    // Servicio que gestiona las operaciones de usuarios.
    private final UserService userService;

/*
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        // Valida y obtiene el usuario
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        return UserDto.from(user);
    }*/


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        //Devolucion
        return ResponseEntity.ok(userService.getUsers().stream()
                .map(UserDto::from)
                .collect(Collectors.toList()));
    }


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/{clienteId}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable Long clienteId) {
        User findUser = userService.getUserById(clienteId).orElse(null);
        //Validacoin existencia
        if(findUser == null){
            return ResponseEntity.notFound().build();
        }
        //Devolucion
        return ResponseEntity.ok(UserDto.from(findUser));
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{ClienteId}")
    public ResponseEntity<UserDto> deleteUser(
            @PathVariable Long clienteId) {
        User findUser = userService.getUserById(clienteId).orElse(null);
        //Validacoin existencia
        if(findUser == null){
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(findUser);
        return ResponseEntity.ok(UserDto.from(findUser));
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping("/{ClienteId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long clienteId,
            @Valid @RequestBody User request) {
        User findUser = userService.getUserById(clienteId).orElse(null);
        //Validacoin existencia
        if(findUser == null){
            return ResponseEntity.notFound().build();
        }
        //Guardado
        userService.saveUser(request);
        return ResponseEntity.ok(UserDto.from(findUser));
    }
}
