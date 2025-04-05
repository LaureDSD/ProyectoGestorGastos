package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.Producto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateTicketRequest(
        @Schema(example = "cliente123") @NotBlank String clienteId,

        @Schema(example = "2025-04-05T13:45:00") LocalDateTime fechaCompra,

        @Schema(description = "Lista de productos definidos por el usuario")
        @NotNull List<Producto> productos
) {
}