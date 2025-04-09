package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Ticket;
import org.antlr.v4.runtime.misc.MultiMap;

import java.util.List;
import java.util.Optional;

public interface TicketService {


    List<Ticket> getAll();

    Optional<Ticket> getByID(Long id);

    Ticket setItem(Ticket o);

    void deleteByID(Long id);

    boolean existsById(Long id);

    List<Ticket> getTicketsByClienteId(Long clienteId);
}

