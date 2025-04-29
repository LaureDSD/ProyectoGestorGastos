package Proyecto.GestorAPI.controllers;


import Proyecto.GestorAPI.exceptions.UpdateImageException;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.UserDto;
import Proyecto.GestorAPI.security.CustomUserDetails;
import Proyecto.GestorAPI.services.StorageService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los usuarios.
 * Permite obtener información del usuario actual, obtener la lista de usuarios,
 * obtener información de un usuario específico y eliminar un usuario.
 * Todas las operaciones están protegidas mediante autenticación y autorización basada en JWT.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management (User Only) ", description = "Gestion de usuarios")
public class UserController {

    private final UserService userService;
    private final StorageService storageService;


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/me")
    public UserDto getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        //Info basica del usaurio
        return UserDto.from(user);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public ResponseEntity<User> getUserData(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        //Obatener la info del usuario
        return ResponseEntity.ok(user);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping
    public ResponseEntity<UserDto> deleteUser(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        //Desactiva la cuenta
        user.setActive(false);
        //Guarda la config
        userService.saveUser(user);
        return ResponseEntity.ok(UserDto.from(user));
    }



    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping("*/newName")
    public ResponseEntity<UserDto> updateName(
            @Valid @RequestBody String request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        //Cambio de nombre
        user.setName(request);
        //Guardar usuario
        userService.saveUser(user);
        return ResponseEntity.ok(UserDto.from(user));
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping("*/newImage")
    public ResponseEntity<UserDto> updateImage(
            @RequestParam(value = "archivo", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        String oldUrl = "";

        //Si no carga imagen
        if(file.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        //Preapracion
        oldUrl = user.getImageUrl();

        try {
            //Cargar imagen
            String newUrl = storageService.updateImageData("profileImage",file);
            user.setImageUrl(newUrl);
            //Guardar url
            userService.saveUser(user);
            //Borrar restos (Opcional)
            storageService.deleteImageData(oldUrl);
            return ResponseEntity.ok(UserDto.from(user));

        }catch (Exception e){
            //return  new UpdateImageException("Error al cargar imagen: "+e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping
    public ResponseEntity<UserDto> updateUser(
            @Valid @RequestBody User request,
            @RequestParam(value = "archivo", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        //Validacoin existencia
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        //Validacion identidaad
        if (user.getId().equals(request.getId())) {
            return ResponseEntity.badRequest().build();
        }
        //Actualizacion de datos

        //Guardado
        userService.saveUser(request);
        return ResponseEntity.ok(UserDto.from(request));
    }
}
