package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    List<Ticket> getTickets();

    List<Ticket> getTicketsContainingText(String text);

    Ticket validateAndGetTicket(Long clienteId);

    Ticket saveTicket(Ticket ticket);

    void deleteTicket(Ticket ticket);

    Optional<Ticket> getTicketsByClienteId(Long clienteId);
}

