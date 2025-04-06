
package Proyecto.GestorAPI.services;

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
    public Optional<Ticket> getByID(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public boolean existsById(Long id) {
        return ticketRepository.existsById(id);
    }

    @Override
    public void saveAll(List<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }


    @Override
    public void deleteTicketById(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public List<Ticket> getTicketsByClienteId(Long clienteId) {
        return ticketRepository.findByUserId(clienteId);
    }


}