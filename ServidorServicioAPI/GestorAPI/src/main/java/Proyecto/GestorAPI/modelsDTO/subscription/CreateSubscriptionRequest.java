package Proyecto.GestorAPI.modelsDTO.subscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateSubscriptionRequest(
        @NotNull Long userId,
        @NotNull String name,
        Long categoriaId ,
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
