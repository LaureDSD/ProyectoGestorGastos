package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.CategoryExpense;

import java.util.List;
import java.util.Optional;

public interface CategoryExpenseService {

    /**
     * Obtiene todas las categorías de gastos.
     *
     * @return Una lista con todas las categorías de gastos disponibles.
     */
    List<CategoryExpense> getAll();

    /**
     * Obtiene una categoría de gasto por su identificador único.
     *
     * @param id El ID de la categoría de gasto a buscar.
     * @return Un objeto Optional que contiene la categoría si se encuentra, o está vacío si no se encuentra.
     */
    Optional<CategoryExpense> getByID(Long id);

    /**
     * Establece o guarda una nueva categoría de gasto (crea o actualiza).
     *
     * @param o La categoría de gasto que se desea guardar.
     * @return La categoría de gasto guardada, que puede incluir un ID generado o actualizado.
     */
    CategoryExpense setItem(CategoryExpense o);

    /**
     * Elimina una categoría de gasto por su identificador único.
     *
     * @param id El ID de la categoría de gasto que se desea eliminar.
     */
    void deleteByID(Long id);

    /**
     * Verifica si existe una categoría de gasto con el identificador proporcionado.
     *
     * @param id El ID de la categoría de gasto.
     * @return true si existe una categoría de gasto con ese ID, false si no existe.
     */
    boolean existsById(Long id);
}

