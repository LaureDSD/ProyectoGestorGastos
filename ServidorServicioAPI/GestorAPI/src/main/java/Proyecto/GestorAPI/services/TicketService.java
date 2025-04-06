package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Ticket;
import org.antlr.v4.runtime.misc.MultiMap;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    List<Ticket> getTickets();

    Ticket saveTicket(Ticket ticket);

    List<Ticket> getTicketsByClienteId(Long clienteId);

    void deleteTicketById(Long ticketId);

    Optional<Ticket> getByID(Long id);

    boolean existsById(Long ticketId);

    void saveAll(List<Ticket> tickets);
}

