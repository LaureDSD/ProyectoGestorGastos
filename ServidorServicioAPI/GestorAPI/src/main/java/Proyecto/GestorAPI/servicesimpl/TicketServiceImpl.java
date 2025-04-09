
package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.repositories.TicketRepository;
import Proyecto.GestorAPI.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Proyecto.GestorAPI.models.Ticket;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository repository;

    @Override
    public List<Ticket> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Ticket> getByID(Long id) {
        return repository.findById(id);
    }

    @Override
    public Ticket setItem(Ticket o) {
        return repository.save(o);
    }

    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<Ticket> getTicketsByClienteId(Long id) {
        return repository.findByUserId(id);
    }


}