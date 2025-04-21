package Proyecto.GestorAPI.modelsDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateTicketRequest(
        @NotNull Long userId,
        @NotNull Long categoriaId,
        @NotNull String store,  // Nueva propiedad para la tienda
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull LocalDateTime fechaCompra,
        @Positive double total,
        String productosJSON
) {
}
