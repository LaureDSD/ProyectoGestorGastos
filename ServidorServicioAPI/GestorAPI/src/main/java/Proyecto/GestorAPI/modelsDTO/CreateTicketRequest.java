package Proyecto.GestorAPI.modelsDTO;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateTicketRequest(
        @Schema(example = "cliente123") @NotBlank Long clienteId,
        @Schema(example = "2025-04-05T13:45:00") LocalDateTime fechaCompra,
        @Schema(description = "Lista de productos definidos por el usuario")
        @NotNull Json productos
) {}