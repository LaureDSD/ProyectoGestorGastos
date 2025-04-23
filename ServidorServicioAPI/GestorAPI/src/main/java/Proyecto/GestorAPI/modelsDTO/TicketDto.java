package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.Ticket;

import java.time.format.DateTimeFormatter;

public record TicketDto(Long id, String store, String format, String name, String description, double total, double iva, String icon, String productsJSON) {

    public static TicketDto from(Ticket ticket) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new TicketDto(
                ticket.getUser().getId(),
                ticket.getStore(),  // Se añade el nombre de la tienda
                formatter.format(ticket.getExpenseDate()),
                //Base
                ticket.getName(),
                ticket.getDescription(),
                ticket.getTotal(),
                ticket.getIva(),
                ticket.getIcon(),
                //Extra
                ticket.getProductsJSON()
        );
    }
}
