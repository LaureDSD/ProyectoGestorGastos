package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.Ticket;

import java.time.format.DateTimeFormatter;

public record TicketDto(Long clienteId, String fechaCompra, String productos) {

    public static TicketDto from(Ticket ticket) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new TicketDto(
                ticket.getUser().getId(),
                formatter.format(ticket.getPurchaseDate()),
                ticket.getProductsJSON()
        );
    }
}
