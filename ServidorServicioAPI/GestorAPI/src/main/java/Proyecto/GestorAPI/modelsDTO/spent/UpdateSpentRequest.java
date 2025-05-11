package Proyecto.GestorAPI.modelsDTO.spent;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateSpentRequest(
        @NotNull Long spentId,
        @NotNull Long userId,
        @NotNull Long categoriaId,
        @NotNull String name,
        String description,
        String icon,
        @NotNull LocalDateTime fechaCompra,
        @Positive double total,
        @Positive double iva,
        @NotNull String typeExpense
) {}
