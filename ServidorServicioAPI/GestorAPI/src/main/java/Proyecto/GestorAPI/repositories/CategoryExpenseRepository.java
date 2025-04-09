package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.CategoryExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para acceder y manipular los datos de las categorías de gastos.
 * Esta interfaz extiende JpaRepository para tener acceso a operaciones CRUD (Crear, Leer, Actualizar, Eliminar) básicas
 * sin necesidad de escribir implementaciones personalizadas.
 */
@Repository
public interface CategoryExpenseRepository extends JpaRepository<CategoryExpense, Long> {
    // JpaRepository proporciona automáticamente las operaciones básicas sobre la entidad CategoryExpense
    // como findAll(), findById(), save(), deleteById(), entre otras.
}
