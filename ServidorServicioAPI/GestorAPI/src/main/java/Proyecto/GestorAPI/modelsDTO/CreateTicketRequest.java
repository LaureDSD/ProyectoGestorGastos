package Proyecto.GestorAPI.modelsDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.time.LocalDateTime;

public record CreateTicketRequest(
        @NotNull Long userId,
        @NotNull Long categoriaId,
        @NotNull LocalDateTime fechaCompra,
        @Positive double total,
        String productosJSON
) {}