package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.repositories.CategoryExpenseRepository;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryExpenseServiceImpl implements CategoryExpenseService {

    // Inyección del repositorio de CategoryExpense para realizar operaciones en la base de datos.
    @Autowired
    private CategoryExpenseRepository repository;

    /**
     * Obtiene todas las categorías de gastos.
     *
     * Este método consulta la base de datos para obtener una lista de todas las categorías de gastos.
     *
     * @return Una lista con todas las categorías de gastos.
     */
    @Override
    public List<CategoryExpense> getAll() {
        return repository.findAll();
    }

    /**
     * Obtiene una categoría de gasto por su ID.
     *
     * Este método busca una categoría de gasto específica a partir de su ID. Si la categoría existe,
     * se devuelve un `Optional` con el objeto `CategoryExpense`. Si no se encuentra, se devuelve un `Optional` vacío.
     *
     * @param id El ID de la categoría de gasto a buscar.
     * @return Un `Optional` que contiene la categoría de gasto si se encuentra, o un `Optional.empty()` si no.
     */
    @Override
    public Optional<CategoryExpense> getByID(Long id) {
        return repository.findById(id);
    }

    /**
     * Guarda o actualiza una categoría de gasto.
     *
     * Este método guarda una nueva categoría de gasto o actualiza una existente en la base de datos. Si el
     * objeto `CategoryExpense` ya existe, se actualizará con la nueva información. Si no, se creará un nuevo
     * registro en la base de datos.
     *
     * @param o El objeto `CategoryExpense` que se desea guardar o actualizar.
     * @return El objeto `CategoryExpense` guardado o actualizado.
     */
    @Override
    public CategoryExpense setItem(CategoryExpense o) {
        return repository.save(o);
    }

    /**
     * Elimina una categoría de gasto por su ID.
     *
     * Este método elimina una categoría de gasto de la base de datos utilizando su ID. Si el ID es válido,
     * la categoría será eliminada. Si no se encuentra la categoría, se lanzará una excepción.
     *
     * @param id El ID de la categoría de gasto a eliminar.
     */
    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica si existe una categoría de gasto con el ID proporcionado.
     *
     * Este método verifica si existe una categoría de gasto en la base de datos con el ID proporcionado.
     *
     * @param id El ID de la categoría de gasto a verificar.
     * @return `true` si existe una categoría con el ID dado, `false` si no existe.
     */
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
