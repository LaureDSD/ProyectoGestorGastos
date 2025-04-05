package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.Producto;
import Proyecto.GestorAPI.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Correct option 1: Find tickets containing a specific Producto entity
    List<Ticket> findByProductos(Producto producto);

    // Correct option 2: Find tickets where any producto has a matching name
    @Query("SELECT t FROM Ticket t JOIN t.productos p WHERE p.nombre LIKE %:productName%")
    List<Ticket> findByProductosContaining(@Param("productName") String productName);

}
