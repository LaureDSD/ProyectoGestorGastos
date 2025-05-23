package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.exceptions.DuplicatedUserInfoException;
import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.user.UserDto;
import Proyecto.GestorAPI.modelsDTO.user.UserMeInDto;
import Proyecto.GestorAPI.modelsDTO.user.UserMeOutDto;
import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.services.SpentService;
import Proyecto.GestorAPI.services.StorageService;
import Proyecto.GestorAPI.services.UserService;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management (User Only)", description = "Gestión de usuarios - operaciones para el usuario autenticado")
public class UserController {

    private final UserService userService;
    private final StorageService storageService;
    private final SpentService spentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    private static final String STORAGE_BASE_PATH = "perfiles/";

    @Operation(
            summary = "Obtener información del usuario autenticado",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @GetMapping("/me")
    public UserMeOutDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        return UserMeOutDto.from(user);
    }

    @Operation(
            summary = "Desactivar el usuario autenticado",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @DeleteMapping("/me")
    public ResponseEntity<UserDto> deleteUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        user.setActive(false);
        userService.saveUser(user);
        return ResponseEntity.ok(UserDto.from(user));
    }

    @Operation(
            summary = "Subir o actualizar la imagen de perfil del usuario autenticado",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @PutMapping("/me/uploadProfile")
    public ResponseEntity<?> uploadProfile(
            @RequestParam("image") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se envió ninguna imagen.");
        }

        try {
            User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
            String oldUrl = user.getImageUrl();

            // Guardar nueva imagen
            String newUrl = storageService.saveImageData(STORAGE_BASE_PATH, file);

            // Eliminar imagen anterior si existe
            if (oldUrl != null && !oldUrl.isEmpty()) {
                storageService.deleteImageData(oldUrl);
            }

            // Actualizar usuario con nueva URL de imagen
            user.setImageUrl(newUrl);
            userService.saveUser(user);

            return ResponseEntity.ok(Map.of("url", newUrl));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar la imagen.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Actualizar datos del usuario autenticado",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @PutMapping("/me")
    public ResponseEntity<UserMeOutDto> updateUser(
            @Valid @RequestBody UserMeInDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Asegurar que el usuario está actualizando su propio perfil
        if (!user.getId().equals(request.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Validar username duplicado si cambia
        if (!user.getUsername().equals(request.getUsername()) &&
                userService.hasUserWithUsername(request.getUsername())) {
            throw new DuplicatedUserInfoException(
                    "Username %s already been used".formatted(request.getUsername()));
        }

        // Validar email duplicado si cambia
        if (!user.getEmail().equals(request.getEmail()) &&
                userService.hasUserWithEmail(request.getEmail())) {
            throw new DuplicatedUserInfoException(
                    "Email %s already been used".formatted(request.getEmail()));
        }

        // Actualizar campos permitidos
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setImageUrl(request.getImageUrl());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setServer(request.getServer());

        userService.saveUser(user);
        return ResponseEntity.ok(UserMeOutDto.from(user));
    }

    @Operation(
            summary = "Cambiar contraseña del usuario autenticado",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @PutMapping("/me/changePassword")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> passwords) {

        String currentPassword = passwords.get("current");
        String newPassword = passwords.get("newPassword");

        User user = userService.validateAndGetUserByUsername(userDetails.getUsername());

        // Validar contraseña actual
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña actual incorrecta.");
        }

        // Actualizar con nueva contraseña encriptada
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        return ResponseEntity.ok().body("Contraseña actualizada.");
    }

    @Operation(
            summary = "Obtener logs de intentos de acceso del usuario autenticado",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @GetMapping("/me/logs")
    public ResponseEntity<List<LoginAttempt>> getUserLoginAttempts(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        List<LoginAttempt> attempts = loginAttemptService.getByUsernameOrEamil(currentUser.getUsername());
        if (attempts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(attempts);
    }

    @Operation(
            summary = "Contar el número total de gastos registrados por el usuario autenticado",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @GetMapping("/me/count")
    public ResponseEntity<Long> countMySpents(@AuthenticationPrincipal CustomUserDetails currentUser) {
        Long userId = userService.validateAndGetUserByUsername(currentUser.getUsername()).getId();
        long count = spentService.countSpentsByUserId(userId);
        return ResponseEntity.ok(count);
    }
}
