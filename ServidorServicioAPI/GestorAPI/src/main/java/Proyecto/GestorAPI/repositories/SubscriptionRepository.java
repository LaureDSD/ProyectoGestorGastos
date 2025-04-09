package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceder y manipular los datos de las suscripciones (Subscription).
 * Esta interfaz extiende JpaRepository para facilitar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la entidad Subscription.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> getByUserId(Long id);
    // JpaRepository proporciona automáticamente las operaciones básicas sobre la entidad Subscription,
    // como findAll(), findById(), save(), deleteById(), entre otras.
}
