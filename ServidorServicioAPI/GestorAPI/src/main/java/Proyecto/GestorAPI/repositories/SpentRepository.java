package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.Spent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceder y manipular los datos de los gastos (Spent).
 * Esta interfaz extiende JpaRepository para tener acceso a operaciones CRUD (Crear, Leer, Actualizar, Eliminar) básicas
 * sin necesidad de escribir implementaciones personalizadas.
 */
@Repository
public interface SpentRepository extends JpaRepository<Spent, Long> {

    List<Spent> getByUserId(Long id);
    // JpaRepository proporciona automáticamente las operaciones básicas sobre la entidad Spent
    // como findAll(), findById(), save(), deleteById(), entre otras.
}
