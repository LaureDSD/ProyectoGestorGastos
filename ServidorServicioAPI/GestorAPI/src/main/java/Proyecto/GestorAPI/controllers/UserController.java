package Proyecto.GestorAPI.controllers;


import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.SpentDto;
import Proyecto.GestorAPI.modelsDTO.UserDto;
import Proyecto.GestorAPI.security.CustomUserDetails;
import Proyecto.GestorAPI.security.RoleServer;
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
@Tag(name = "User Management (Verify control) ", description = "Gestion de usuarios")
public class UserController {

    // Servicio que gestiona las operaciones de usuarios.
    private final UserService userService;


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        // Valida y obtiene el usuario
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        return UserDto.from(user);
    }


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        //Validacion
        if(user.getRole() != RoleServer.ADMIN){
            return ResponseEntity.noContent().build();
        }
        //Devolucion
        return ResponseEntity.ok(userService.getUsers().stream()
                .map(UserDto::from)
                .collect(Collectors.toList()));
    }


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/{clienteId}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable Long clienteId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        User findUser = userService.getUserById(clienteId).orElse(null);
        //Validacoin existencia
        if(findUser == null){
            return ResponseEntity.notFound().build();
        }
        //Si es admin
        if(user.getRole() == RoleServer.ADMIN ){
            return ResponseEntity.ok(UserDto.from(findUser));
        }
        //Validacion propiedad usuario
        if(( !user.getId().equals(findUser.getId()) || !user.getId().equals(clienteId))){
            return ResponseEntity.badRequest().build();
        }
        //Devolucion
        return ResponseEntity.ok(UserDto.from(user));
    }


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{ClienteId}")
    public ResponseEntity<UserDto> deleteUser(
            @PathVariable Long clienteId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        User findUser = userService.getUserById(clienteId).orElse(null);
        //Validacoin existencia
        if(findUser == null){
            return ResponseEntity.notFound().build();
        }
        //Si es admin
        if(user.getRole() == RoleServer.ADMIN ){
            userService.deleteUser(findUser);
            return ResponseEntity.ok(UserDto.from(user));
        }
        //Validacion propiedad usuario
        if(( !user.getId().equals(findUser.getId()) || !user.getId().equals(clienteId))){
            return ResponseEntity.badRequest().build();
        }
        //Desactivacion de la cuenta
        user.setActive(false);
        userService.saveUser(user);
        return ResponseEntity.ok(UserDto.from(user));
    }

    //update
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping("/{ClienteId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long clienteId,
            @Valid @RequestBody User request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        User findUser = userService.getUserById(clienteId).orElse(null);
        //Validacoin existencia
        if(findUser == null){
            return ResponseEntity.notFound().build();
        }
        //Si es admin y no coinciden idcliente y request
        if(user.getRole() == RoleServer.ADMIN && !findUser.getId().equals(request.getId()) ){
            return ResponseEntity.badRequest().build();
        }
        //Si es usuario y no cumle triple verificacion
        if(( !user.getId().equals(findUser.getId()) || !request.getId().equals(clienteId))){
            return ResponseEntity.badRequest().build();
        }
        //Guardado
        userService.saveUser(request);
        return ResponseEntity.ok(UserDto.from(user));
    }
}
