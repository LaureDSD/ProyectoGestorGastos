package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.Ticket;

import java.time.format.DateTimeFormatter;

public record TicketDto(Long clienteId, String store, String fechaCompra, String productos) {

    public static TicketDto from(Ticket ticket) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new TicketDto(
                ticket.getUser().getId(),
                ticket.getStore(),  // Se añade el nombre de la tienda
                formatter.format(ticket.getExpenseDate()),
                ticket.getProductsJSON()
        );
    }
}
