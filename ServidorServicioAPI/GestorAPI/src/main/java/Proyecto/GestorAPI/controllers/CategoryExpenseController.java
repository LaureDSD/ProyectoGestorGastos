package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.category.CategoryExpenseDto;
import Proyecto.GestorAPI.security.CustomUserDetails;
import Proyecto.GestorAPI.security.RoleServer;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

    /**
     * Obtener todas las categorías de gasto.
     *
     * @return Lista de categorías de gasto.
     */
    @GetMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todas las categorías de gasto"
    )
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

    /**
     * Crear una nueva categoría de gasto.
     *
     * @param request La categoría de gasto a crear.
     * @return La categoría de gasto creada.
     */
    @PostMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Crear una nueva categoría de gasto"
    )
    public ResponseEntity<CategoryExpenseDto> createCategory(
            @Valid @RequestBody CategoryExpenseDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        // Solo los admins pueden crear categorías de gasto.
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

    /**
     * Actualizar una categoría de gasto por ID.
     *
     * @param categoryId El ID de la categoría a actualizar.
     * @param request Los datos de la categoría de gasto a actualizar.
     * @return La categoría de gasto actualizada.
     */
    @PutMapping("/{categoryId}")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Actualizar una categoría de gasto"
    )
    public ResponseEntity<CategoryExpenseDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryExpenseDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        // Solo los admins pueden actualizar categorías de gasto.
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

    /**
     * Eliminar una categoría de gasto por ID.
     *
     * @param categoryId El ID de la categoría de gasto a eliminar.
     * @return Respuesta sin contenido si la eliminación fue exitosa.
     */
    @DeleteMapping("/{categoryId}")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Eliminar una categoría de gasto"
    )
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        // Solo los admins pueden eliminar categorías de gasto.
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
