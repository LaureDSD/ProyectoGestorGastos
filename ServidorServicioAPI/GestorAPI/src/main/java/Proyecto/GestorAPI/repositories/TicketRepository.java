package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceder y manipular los datos de los tickets (Ticket).
 * Esta interfaz extiende JpaRepository para facilitar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la entidad Ticket.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Método para encontrar todos los tickets asociados a un usuario específico.
     *
     * @param clienteId el ID del usuario (cliente) para el que se buscan los tickets.
     * @return una lista de tickets relacionados con el usuario indicado.
     */
    List<Ticket> findByUserId(Long clienteId);
}
