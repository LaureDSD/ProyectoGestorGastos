package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Spent;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.CreateSpentRequest;
import Proyecto.GestorAPI.modelsDTO.SpentDto;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import Proyecto.GestorAPI.services.SpentService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gastos")
@Tag(name = "Gasto Management", description = "Operaciones sobre los gastos registrados")
public class SpentController {

    private final SpentService spentService;
    private final UserService userService;
    private final CategoryExpenseService categoriaService;

    @GetMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todos los gastos filtrados por clienteId (opcional)"
    )
    public ResponseEntity<List<SpentDto>> getSpents(
            @RequestParam(value = "clienteId", required = false) Long clienteId) {

        List<Spent> spents = (clienteId != null)
                ? spentService.getSpentsByUserId(clienteId)
                : spentService.getAll();

        if (spents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(spents.stream()
                .map(SpentDto::from)
                .collect(Collectors.toList()));
    }

    @PostMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Crear un nuevo gasto"
    )
    public ResponseEntity<SpentDto> createSpent(
            @Valid @RequestBody CreateSpentRequest request) {

        // Crear el gasto con referencias por ID
        Spent spent = new Spent();
        spent.setUser(userService.getUserById(request.userId()).orElse(new User()));
        spent.setCategory(categoriaService.getByID(request.categoriaId()).orElse(null));
        spent.setExpenseDate(request.fechaCompra());
        spent.setTotal(request.total());
        spent.setIva(request.iva());
        spent.setName(request.name());
        spent.setDescription(request.description());
        spent.setIcon(request.icon());
        spent.setCreatedAt(LocalDateTime.now());

        Spent createdSpent = spentService.setItem(spent);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SpentDto.from(createdSpent));
    }

    @DeleteMapping("/{spentId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Eliminar un gasto por ID"
    )
    public ResponseEntity<Void> deleteSpent(@PathVariable Long spentId) {
        if (!spentService.existsById(spentId)) {
            return ResponseEntity.notFound().build();
        }
        spentService.deleteByID(spentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{spentId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar un gasto por ID"
    )
    public ResponseEntity<SpentDto> updateSpent(
            @PathVariable Long spentId,
            @Valid @RequestBody Spent spent) {

        if (spent.getSpent_id() == null || !spent.getSpent_id().equals(spentId)) {
            return ResponseEntity.badRequest().build();
        }

        Spent updatedSpent = spentService.setItem(spent);
        return ResponseEntity.ok(SpentDto.from(updatedSpent));
    }
}
