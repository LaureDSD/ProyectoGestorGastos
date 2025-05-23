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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/api/contacto")
@Tag(name = "Contacto (Open)", description = "Operaciones relacionadas con formularios de contacto del sistema")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @Autowired
    private UserService userService;

    /**
     * Guarda un formulario de contacto. El campo 'revisado' se inicializa en falso.
     * Requiere autenticación con token Bearer.
     */
    @PostMapping("")
    @Operation(
            summary = "Guardar formulario de contacto",
            description = "Crea un nuevo formulario de contacto y lo guarda en la base de datos. El campo 'revisado' se establece en false por defecto.",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario guardado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FormContacto.class)))
    })
    public ResponseEntity<FormContacto> guardar(@RequestBody FormContacto contacto) {
        contacto.setRevisado(false);
        return ResponseEntity.ok(contactoService.setItem(contacto));
    }

    /**
     * Devuelve todos los formularios de contacto.
     * Solo accesible por usuarios con rol ADMIN.
     */
    @GetMapping("")
    @Operation(
            summary = "Obtener todos los formularios de contacto",
            description = "Devuelve una lista con todos los formularios registrados. Solo accesible por administradores.",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de formularios encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FormContacto.class))),
            @ApiResponse(responseCode = "204", description = "No hay formularios disponibles"),
            @ApiResponse(responseCode = "400", description = "Usuario no autenticado o sin permisos")
    })
    public ResponseEntity<List<FormContacto>> obtenerTodos(@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if (user.getRole() != RoleServer.ADMIN) {
            return ResponseEntity.badRequest().build();
        }

        List<FormContacto> contactos = contactoService.getAll();

        if (contactos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contactos);
    }

    /**
     * Devuelve un formulario de contacto según su ID.
     * Solo accesible por usuarios con rol ADMIN.
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener formulario por ID",
            description = "Obtiene un formulario de contacto específico por su ID. Solo accesible por administradores.",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FormContacto.class))),
            @ApiResponse(responseCode = "404", description = "Formulario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Usuario no autenticado o sin permisos")
    })
    public ResponseEntity<FormContacto> obtenerPorId(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if (user.getRole() != RoleServer.ADMIN) {
            return ResponseEntity.badRequest().build();
        }

        return contactoService.getByID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un formulario de contacto por ID.
     * Solo accesible por usuarios con rol ADMIN.
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar formulario por ID",
            description = "Elimina un formulario de contacto identificado por su ID. Solo accesible por administradores.",
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Formulario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Formulario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Usuario no autenticado o sin permisos")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if (user.getRole() != RoleServer.ADMIN) {
            return ResponseEntity.badRequest().build();
        }
        if (contactoService.existsById(id)) {
            contactoService.deleteByID(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
