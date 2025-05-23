package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.category.CategoryExpenseDto;
import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Category Expense Management", description = "Operaciones sobre las categorías de gasto")
public class CategoryExpenseController {

    private final CategoryExpenseService categoryExpenseService;
    private final UserService userService;

    @GetMapping("")
    @Operation(
            summary = "Obtener todas las categorías de gasto",
            description = "Devuelve una lista de todas las categorías disponibles para el usuario autenticado.",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías de gasto",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryExpenseDto.class))),
            @ApiResponse(responseCode = "204", description = "No hay categorías disponibles"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<List<CategoryExpenseDto>> getAllCategories(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        List<CategoryExpense> categories = categoryExpenseService.getAll();

        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(categories.stream()
                .map(CategoryExpenseDto::from)
                .collect(Collectors.toList()));
    }

    @PostMapping("/")
    @Operation(
            summary = "Crear una nueva categoría de gasto",
            description = "Solo administradores pueden crear nuevas categorías de gasto.",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(schema = @Schema(implementation = CategoryExpenseDto.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: solo administradores"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<CategoryExpenseDto> createCategory(
            @Valid
            @RequestBody(
                    required = true,
                    description = "Datos de la nueva categoría de gasto"
            ) CategoryExpenseDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if (user.getRole() != RoleServer.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CategoryExpense categoryExpense = new CategoryExpense();
        categoryExpense.setName(request.name());
        categoryExpense.setDescription(request.description());
        categoryExpense.setIva(request.iva());

        CategoryExpense createdCategory = categoryExpenseService.setItem(categoryExpense);

        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryExpenseDto.from(createdCategory));
    }

    @PutMapping("/{categoryId}")
    @Operation(
            summary = "Actualizar una categoría de gasto",
            description = "Solo administradores pueden actualizar las categorías existentes.",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = CategoryExpenseDto.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<CategoryExpenseDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryExpenseDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if (user.getRole() != RoleServer.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CategoryExpense categoryExpense = categoryExpenseService.getByID(categoryId)
                .orElse(null);

        if (categoryExpense == null) {
            return ResponseEntity.notFound().build();
        }

        categoryExpense.setName(request.name());
        categoryExpense.setDescription(request.description());
        categoryExpense.setIva(request.iva());

        CategoryExpense updatedCategory = categoryExpenseService.setItem(categoryExpense);

        return ResponseEntity.ok(CategoryExpenseDto.from(updatedCategory));
    }

    @DeleteMapping("/{categoryId}")
    @Operation(
            summary = "Eliminar una categoría de gasto",
            description = "Elimina una categoría por ID. Solo accesible por administradores.",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if (user.getRole() != RoleServer.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CategoryExpense categoryExpense = categoryExpenseService.getByID(categoryId)
                .orElse(null);

        if (categoryExpense == null) {
            return ResponseEntity.notFound().build();
        }

        categoryExpenseService.deleteByID(categoryId);
        return ResponseEntity.noContent().build();
    }
}
