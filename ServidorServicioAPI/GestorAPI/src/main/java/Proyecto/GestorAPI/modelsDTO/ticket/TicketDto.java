package Proyecto.GestorAPI.modelsDTO.ticket;

import Proyecto.GestorAPI.models.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record TicketDto(
        Long spentId,
        Long userId,
        Long categoriaId,
        String store,
        LocalDateTime fechaCompra,
        String name,
        String description,
        double total,
        double iva,
        String icon,
        String productsJSON
) {
    public static TicketDto from(Ticket ticket) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new TicketDto(
                ticket.getSpentId(),
                ticket.getUser().getId(),
                ticket.getCategory().getId(),
                ticket.getStore(),
                ticket.getExpenseDate(),
                ticket.getName(),
                ticket.getDescription(),
                ticket.getTotal(),
                ticket.getIva(),
                ticket.getIcon(),
                ticket.getProductsJSON()
        );
    }
}