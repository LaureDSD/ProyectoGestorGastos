package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.exceptions.DuplicatedUserInfoException;
import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.user.UserDto;
import Proyecto.GestorAPI.modelsDTO.user.UserMeInDto;
import Proyecto.GestorAPI.modelsDTO.user.UserMeOutDto;
import Proyecto.GestorAPI.security.CustomUserDetails;
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
@Tag(name = "User Management (User Only)", description = "Gestion de usuarios")
public class UserController {

    private final UserService userService;
    private final StorageService storageService;
    private final SpentService spentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    private static final String STORAGE_BASE_PATH = "perfiles/";

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/me")
    public UserMeOutDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        return UserMeOutDto.from(user);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/me")
    public ResponseEntity<UserDto> deleteUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        user.setActive(false);
        userService.saveUser(user);
        return ResponseEntity.ok(UserDto.from(user));
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping("/me/uploadProfile")
    public ResponseEntity<?> uploadProfile(
            @RequestParam(value = "image") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se envió ninguna imagen.");
        }

        try {
            User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
            String oldUrl = user.getImageUrl();
            String newUrl = storageService.saveImageData(STORAGE_BASE_PATH, file);
            storageService.deleteImageData(oldUrl);

            //Actualizar usuario
            user.setImageUrl(newUrl);
            userService.saveUser(user);

            return ResponseEntity.ok(Map.of("url", newUrl));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar la imagen.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<UserMeOutDto> updateUser(
            @Valid @RequestBody UserMeInDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getId().equals(request.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!user.getUsername().equals(request.getUsername()) &&
                userService.hasUserWithUsername(request.getUsername())) {
            throw new DuplicatedUserInfoException("Username %s already been used".formatted(request.getUsername()));
        }

        if (!user.getEmail().equals(request.getEmail()) &&
                userService.hasUserWithEmail(request.getEmail())) {
            throw new DuplicatedUserInfoException("Email %s already been used".formatted(request.getEmail()));
        }

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

    @PutMapping("/me/changePassword")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody Map<String, String> passwords) {
        String currentPassword = passwords.get("current");
        String newPassword = passwords.get("newPassword");

        User user = userService.validateAndGetUserByUsername(userDetails.getUsername());
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña actual incorrecta.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);
        return ResponseEntity.ok().body("Contraseña actualizada.");
    }

    @Operation(
            summary = "Obtener logs de acceso del usuario actual",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME))
    @GetMapping("/me/logs")
    public ResponseEntity<List<LoginAttempt>> getUserLoginAttempts(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<LoginAttempt> attempts = loginAttemptService.getByUsernameOrEamil(currentUser.getUsername());
        if (attempts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(attempts);
    }

    @GetMapping("/me/count")
    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    public ResponseEntity<Long> countMySpents(@AuthenticationPrincipal CustomUserDetails currentUser) {
        //Puedo optimizarlo , si, pero no funciona con otras bd si lo hago
        Long userId = userService.validateAndGetUserByUsername(currentUser.getUsername()).getId();
        long count = spentService.countSpentsByUserId(userId);
        return ResponseEntity.ok(count);
    }
}
