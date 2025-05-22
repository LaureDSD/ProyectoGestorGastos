package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.FormContacto;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.services.ContactoService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/api/contacto")
@Tag(name = "Contacto (Open)", description = "Formularios de contacto")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Guardar formulario"
    )
    public ResponseEntity<FormContacto> guardar(@RequestBody FormContacto contacto) {
        contacto.setRevisado(false);
        return ResponseEntity.ok(contactoService.setItem(contacto));
    }

    @GetMapping("")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener formularios"
    )
    public ResponseEntity<List<FormContacto>> obtenerTodos(@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if( user.getRole() != RoleServer.ADMIN ){
            return ResponseEntity.badRequest().build();
        }

        List<FormContacto> contactos  = contactoService.getAll();

        if (contactos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contactos);
    }

    @GetMapping("/{id}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener formularios por ID"
    )
    public ResponseEntity<FormContacto> obtenerPorId(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if( user.getRole() != RoleServer.ADMIN ){
            return ResponseEntity.badRequest().build();
        }

        return contactoService.getByID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Borrar formulario por ID"
    )
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if( user.getRole() != RoleServer.ADMIN ){
            return ResponseEntity.badRequest().build();
        }
        if (contactoService.existsById(id)) {
            contactoService.deleteByID(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



}

