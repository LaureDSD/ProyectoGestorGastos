package Proyecto.GestorAPI.modelsDTO.spent;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

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
        @PositiveOrZero double iva,
        @NotNull String typeExpense
) {}
