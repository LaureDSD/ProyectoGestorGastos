package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.Spent;
import Proyecto.GestorAPI.repositories.SpentRepository;
import Proyecto.GestorAPI.services.SpentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpentServiceImpl implements SpentService {

    // Inyección del repositorio de Spent para realizar operaciones en la base de datos.
    @Autowired
    private SpentRepository repository;

    /**
     * Obtiene todos los gastos registrados.
     *
     * Este método consulta la base de datos para obtener una lista de todos los objetos `Spent`.
     *
     * @return Una lista con todos los gastos registrados en la base de datos.
     */
    @Override
    public List<Spent> getAll() {
        return repository.findAll();
    }

    /**
     * Obtiene un gasto por su ID.
     *
     * Este método busca un gasto específico a partir de su ID. Si el gasto existe,
     * se devuelve un `Optional` con el objeto `Spent`. Si no se encuentra, se devuelve un `Optional` vacío.
     *
     * @param id El ID del gasto a buscar.
     * @return Un `Optional` que contiene el gasto si se encuentra, o un `Optional.empty()` si no se encuentra.
     */
    @Override
    public Optional<Spent> getByID(Long id) {
        return repository.findById(id);
    }

    /**
     * Guarda o actualiza un gasto.
     *
     * Este método guarda un nuevo gasto o actualiza un gasto existente en la base de datos. Si el
     * objeto `Spent` ya existe, se actualizará con la nueva información. Si no, se creará un nuevo
     * registro en la base de datos.
     *
     * @param o El objeto `Spent` que se desea guardar o actualizar.
     * @return El objeto `Spent` guardado o actualizado.
     */
    @Override
    public Spent setItem(Spent o) {
        return repository.save(o);
    }

    /**
     * Elimina un gasto por su ID.
     *
     * Este método elimina un gasto de la base de datos utilizando su ID. Si el ID es válido,
     * el gasto será eliminado. Si no se encuentra el gasto, se lanzará una excepción.
     *
     * @param id El ID del gasto a eliminar.
     */
    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica si existe un gasto con el ID proporcionado.
     *
     * Este método verifica si existe un gasto en la base de datos con el ID proporcionado.
     *
     * @param id El ID del gasto a verificar.
     * @return `true` si existe un gasto con el ID dado, `false` si no existe.
     */
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    //Falta comentar
    @Override
    public List<Spent> getSpentsByUserId(Long id) {
        return  repository.getByUserId(id);
    }
}
