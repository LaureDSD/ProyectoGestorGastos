package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Spent;
import Proyecto.GestorAPI.models.Subscription;
import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.modelsDTO.spent.CreateSpentRequest;
import Proyecto.GestorAPI.modelsDTO.spent.SpentDto;
import Proyecto.GestorAPI.modelsDTO.spent.SpentFullDto;
import Proyecto.GestorAPI.modelsDTO.spent.UpdateSpentRequest;
import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import Proyecto.GestorAPI.services.SpentService;
import Proyecto.GestorAPI.services.UserService;
import Proyecto.GestorAPI.servicesimpl.StorageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador REST para la gestión de gastos ("Spent").
 * Permite realizar operaciones CRUD sobre gastos registrados,
 * incluyendo carga de imágenes asociadas.
 *
 * La seguridad se maneja mediante autenticación JWT con Bearer Token,
 * y el acceso se controla por roles de usuario (ADMIN y USER).
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gastos")
@Tag(name = "Spent Management (Verify control)", description = "Operaciones sobre los gastos registrados")
public class SpentController {

    @Autowired
    private SpentService spentService;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageServiceImpl storageService;

    private static final String STORAGE_BASE_PATH = "gastos/";

    /**
     * Obtiene todos los gastos filtrados opcionalmente por clienteId.
     *
     * - Si el usuario es ADMIN puede obtener todos los gastos o los de un cliente específico.
     * - Si el usuario no es ADMIN solo puede obtener sus propios gastos.
     *
     * @param clienteId Id del cliente para filtrar gastos (opcional).
     * @param currentUser Usuario autenticado actual.
     * @return Lista de gastos en formato DTO, o 204 si no existen gastos.
     */
    @GetMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todos los gastos filtrados por clienteId (opcional para admins)"
    )
    public ResponseEntity<List<SpentDto>> getSpents(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        List<Spent> spents = new ArrayList<>();

        if (user.getRole() != RoleServer.ADMIN) {
            // No admin: obtener gastos del usuario autenticado
            spents = spentService.getSpentsByUserId(user.getId());
        } else {
            // Admin: obtener gastos del clienteId si se provee, o todos si no
            spents = (clienteId != null)
                    ? spentService.getSpentsByUserId(clienteId)
                    : spentService.getAll();
        }

        // Verificación de existencia
        if (spents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Mapear a DTO y devolver lista
        return ResponseEntity.ok(spents.stream()
                .map(SpentDto::from)
                .collect(Collectors.toList()));
    }

    /**
     * Obtiene todos los gastos con información completa,
     * opcionalmente filtrados por clienteId.
     *
     * Similar a getSpents pero devuelve DTO con detalles extendidos.
     *
     * @param clienteId Id del cliente para filtrar gastos (opcional).
     * @param currentUser Usuario autenticado actual.
     * @return Lista de gastos con detalles completos, o 204 si no hay gastos.
     */
    @GetMapping("/fullspents")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todos los gastos"
    )
    public ResponseEntity<List<SpentFullDto>> getAllSpents(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        List<Spent> spents = new ArrayList<>();

        if (user.getRole() != RoleServer.ADMIN) {
            spents = spentService.getSpentsByUserId(user.getId());
        } else {
            spents = (clienteId != null)
                    ? spentService.getSpentsByUserId(clienteId)
                    : spentService.getAll();
        }

        if (spents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Mapear la lista de gastos a lista de DTOs completos
        List<SpentFullDto> result = spentService.mappingSpentFullDtosList(spents);

        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene un gasto por su ID.
     *
     * Solo puede acceder el ADMIN o el dueño del gasto.
     *
     * @param spentId ID del gasto a obtener
     * @param currentUser Usuario autenticado actual
     * @return DTO del gasto si existe y tiene permiso, 404 o 403 si no
     */
    @GetMapping("/{spentId}")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener un gasto por ID"
    )
    public ResponseEntity<SpentDto> getSpentById(
            @PathVariable Long spentId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Spent spent = spentService.getByID(spentId).orElse(null);

        // Verificación de existencia
        if (spent == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificación de propiedad o rol ADMIN
        if (user.getRole() != RoleServer.ADMIN && !spent.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(SpentDto.from(spent));
    }

    /**
     * Crea un nuevo gasto.
     *
     * Si el usuario es ADMIN puede asignar gasto a otro cliente mediante clienteId.
     * Si no es ADMIN, el gasto se asigna automáticamente al usuario autenticado.
     *
     * @param clienteId Id del cliente para asignar gasto (opcional)
     * @param request Datos para crear el gasto
     * @param currentUser Usuario autenticado actual
     * @return DTO del gasto creado con código 201 CREATED
     */
    @PostMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Crear un nuevo gasto"
    )
    public ResponseEntity<SpentDto> createSpent(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @Valid @RequestBody CreateSpentRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Spent spent;

        if (user.getRole() != RoleServer.ADMIN) {
            spent = spentService.mappingSpent(request, user.getId());
        } else {
            spent = spentService.mappingSpent(request, (clienteId != null) ? clienteId : user.getId());
        }

        Spent createdSpent = spentService.setItem(spent);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SpentDto.from(createdSpent));
    }

    /**
     * Elimina un gasto por ID.
     *
     * Solo el ADMIN o el dueño del gasto pueden eliminarlo.
     *
     * @param spentId ID del gasto a eliminar
     * @param currentUser Usuario autenticado actual
     * @return 204 No Content si eliminado, 404 si no existe, 403 si sin permiso
     */
    @DeleteMapping("/{spentId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Eliminar un gasto por ID"
    )
    public ResponseEntity<Void> deleteSpent(
            @PathVariable Long spentId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Spent spent = spentService.getByID(spentId).orElse(null);

        if (spent == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getRole() != RoleServer.ADMIN && !spent.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        spentService.deleteByID(spentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza un gasto por ID.
     *
     * Solo el ADMIN o el dueño del gasto pueden actualizarlo.
     *
     * @param spentId ID del gasto a actualizar
     * @param request Datos para actualizar el gasto
     * @param currentUser Usuario autenticado actual
     * @return DTO del gasto actualizado o errores 404/403
     */
    @PutMapping("/{spentId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar un gasto por ID"
    )
    public ResponseEntity<SpentDto> updateSpent(
            @PathVariable Long spentId,
            @Valid @RequestBody UpdateSpentRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Spent spent = spentService.getByID(spentId).orElse(null);

        // Verificación existencia
        if (spent == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificación de propiedad o rol ADMIN
        if (user.getRole() != RoleServer.ADMIN && !spent.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Spent updatedSpent = spentService.setItem(spentService.mappingUpdateSpent(request, spent.getUser().getId()));
        return ResponseEntity.ok(SpentDto.from(updatedSpent));
    }



    /**
     * Endpoint para subir una imagen asociada a un gasto.
     *
     * - Recibe una imagen multipart y el ID del gasto.
     * - Valida la imagen y actualiza la URL del icono en el gasto.
     * - El archivo se guarda en directorio /gastos/{spentId}/
     *
     * @param spentId ID del gasto
     * @param file Imagen a subir
     * @param currentUser Usuario autenticado
     * @return DTO actualizado con la URL del icono, o errores 404/400/403
     * @throws IOException si falla la subida
     */
    @PostMapping(value = "/uploadimg/{spentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Subir imagen para un gasto"
    )
    public ResponseEntity<?> uploadSpenseImageWithData(
            @RequestPart("image") MultipartFile file,
            @RequestParam("spentId") Long spentId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se envió ninguna imagen.");
        }
        try {
            // Procesar el usuario
            User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
            Spent spent = spentService.getByID(Long.valueOf(spentId)).orElse(null);
            String oldUrl = spent.getIcon();
            String newUrl = storageService.saveImageData(STORAGE_BASE_PATH, file);

            if (oldUrl != null && !oldUrl.isEmpty()) {
                storageService.deleteImageData(oldUrl);
            }
            spent.setIcon( newUrl );
            spentService.setItem(spent);
            return ResponseEntity.ok(Map.of("url", newUrl));

        } catch (IOException e) {return ResponseEntity.internalServerError().body("Error al guardar la imagen.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

}
