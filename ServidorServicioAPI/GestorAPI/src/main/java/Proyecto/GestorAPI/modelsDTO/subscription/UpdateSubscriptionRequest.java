package Proyecto.GestorAPI.modelsDTO.subscription;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateSubscriptionRequest(
        @NotNull Long spentId,
        @NotNull Long userId,
        Long categoriaId ,
        @NotNull String name,
        String description,
        String icon,
        @NotNull LocalDateTime fechaCompra,
        @Positive double total,
        @Positive double iva,
        @NotNull LocalDateTime start,
        LocalDateTime end,
        @Positive double accumulate,
        @Positive int restartDay,
        @Positive int intervalTime,
        @NotNull boolean activa

) {}
