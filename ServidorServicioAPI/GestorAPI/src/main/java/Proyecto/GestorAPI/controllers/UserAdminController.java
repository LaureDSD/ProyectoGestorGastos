package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.user.UserDto;
import Proyecto.GestorAPI.services.StorageService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador REST para la gestión de usuarios en la aplicación.
 *
 * <p>
 * Proporciona endpoints para obtener la lista de usuarios, obtener detalles de un usuario específico,
 * actualizar información, eliminar usuarios, y obtener datos protegidos para el administrador.
 * Todas las operaciones requieren autenticación y autorización mediante JWT con esquema Bearer Token.
 * </p>
 *
 * <p><b>Ruta base:</b> /api/users</p>
 *
 * <p><b>Seguridad:</b> Se requiere token JWT en el header de autorización para acceder a todos los endpoints.</p>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management (Admin only)", description = "Gestión de usuarios")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    /**
     * Obtiene la lista completa de usuarios registrados en el sistema.
     *
     * @return ResponseEntity con la lista de usuarios. Código 200 OK con lista, o vacía si no hay usuarios.
     */
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener lista de todos los usuarios"
    )
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(new ArrayList<>(userService.getUsers()));
    }

    /**
     * Obtiene los detalles de un usuario específico identificado por su ID.
     *
     * @param clienteId ID del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado (200 OK), o 404 Not Found si no existe.
     */
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener un usuario por ID"
    )
    @GetMapping("/{clienteId}")
    public ResponseEntity<User> getUser(
            @PathVariable Long clienteId) {
        User findUser = userService.getUserById(clienteId).orElse(null);
        if(findUser == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findUser);
    }

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param clienteId ID del usuario a eliminar.
     * @return ResponseEntity con el usuario eliminado (200 OK), o 404 Not Found si no existe.
     */
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Eliminar un usuario por ID"
    )
    @DeleteMapping("/{clienteId}")
    public ResponseEntity<User> deleteUser(
            @PathVariable Long clienteId) {
        User findUser = userService.getUserById(clienteId).orElse(null);

        if(findUser!=null) {
            try {
                //Borrar imagen
                storageService.deleteImageData(findUser.getImageUrl());
            }catch (Exception e){
                //
            }
            //Borrar usuario
            userService.deleteUser(findUser);
        }else{
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findUser);
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param clienteId ID del usuario a actualizar.
     * @param request   Objeto User con los nuevos datos para actualizar.
     * @return ResponseEntity con el usuario actualizado (200 OK), o 404 Not Found si el usuario no existe.
     */
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar un usuario por ID"
    )
    @PutMapping("/{clienteId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long clienteId,
            @Valid @RequestBody User request) {
        User findUser = userService.getUserById(clienteId).orElse(null);
        // Validación de existencia
        if(findUser == null){
            return ResponseEntity.notFound().build();
        }
        // Guardar usuario con los nuevos datos
        userService.saveUser(request);
        return ResponseEntity.ok(UserDto.from(findUser));
    }

    /**
     * Obtiene información protegida para administradores basada en el usuario autenticado.
     * Incluye el nombre de usuario, roles y un mensaje de autorización.
     *
     * @param userDetails Detalles del usuario autenticado obtenidos del contexto de seguridad.
     * @return ResponseEntity con un mapa de datos protegidos (200 OK).
     */
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener datos protegidos del dashboard para el usuario autenticado"
    )
    @GetMapping("/data")
    public ResponseEntity<?> getAdminData(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", userDetails.getUsername());
        data.put("rol", userDetails.getAuthorities());
        data.put("message", "Acceso autorizado a datos protegidos del dashboard.");
        return ResponseEntity.ok(data);
    }

}
