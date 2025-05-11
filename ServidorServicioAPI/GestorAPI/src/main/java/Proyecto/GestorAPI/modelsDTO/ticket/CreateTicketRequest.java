package Proyecto.GestorAPI.modelsDTO.ticket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTicketRequest{
        private Long userId;
        private Long categoriaId;
        private String store;
        private LocalDateTime fechaCompra;
        private String name;
        private String description;
        private double total;
        private double iva;
        private String icon;
        private String productsJSON;
}
