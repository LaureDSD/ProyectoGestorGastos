
package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.exceptions.TicketNotFoundException;
import Proyecto.GestorAPI.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import Proyecto.GestorAPI.models.Ticket;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getTicketsContainingText(String text) {
        return ticketRepository.findByProductosContaining(text);
    }

    @Override
    public Ticket validateAndGetTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(String.format("Ticket with clienteId %s not found", ticketId)));
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public Optional<Ticket> getTicketsByClienteId(Long clienteId) {
        return ticketRepository.findById(clienteId);
    }
}