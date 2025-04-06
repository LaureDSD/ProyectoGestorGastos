package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.Ticket;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record TicketDto(Long clienteId, String fechaCompra, String productos) {

    public static TicketDto from(Ticket ticket) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());
        return new TicketDto(
                ticket.getUser().getId(),
                formatter.format(ticket.getFechaCompra()),
                ticket.getProductosJSON()
        );
    }
}
