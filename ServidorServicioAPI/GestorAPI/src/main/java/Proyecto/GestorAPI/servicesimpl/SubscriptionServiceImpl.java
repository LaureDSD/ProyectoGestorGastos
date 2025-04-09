package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.Subscription;
import Proyecto.GestorAPI.repositories.SubscriptionRepository;
import Proyecto.GestorAPI.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    // Inyección del repositorio de Subscription para realizar operaciones en la base de datos.
    @Autowired
    private SubscriptionRepository repository;

    /**
     * Obtiene todas las suscripciones registradas.
     *
     * Este método consulta la base de datos para obtener una lista de todas las suscripciones.
     *
     * @return Una lista con todas las suscripciones registradas en la base de datos.
     */
    @Override
    public List<Subscription> getAll() {
        return repository.findAll();
    }

    /**
     * Obtiene una suscripción por su ID.
     *
     * Este método busca una suscripción específica a partir de su ID. Si la suscripción existe,
     * se devuelve un `Optional` con el objeto `Subscription`. Si no se encuentra, se devuelve un `Optional` vacío.
     *
     * @param id El ID de la suscripción a buscar.
     * @return Un `Optional` que contiene la suscripción si se encuentra, o un `Optional.empty()` si no se encuentra.
     */
    @Override
    public Optional<Subscription> getByID(Long id) {
        return repository.findById(id);
    }

    /**
     * Guarda o actualiza una suscripción.
     *
     * Este método guarda una nueva suscripción o actualiza una suscripción existente en la base de datos. Si el
     * objeto `Subscription` ya existe, se actualizará con la nueva información. Si no, se creará un nuevo
     * registro en la base de datos.
     *
     * @param o El objeto `Subscription` que se desea guardar o actualizar.
     * @return El objeto `Subscription` guardado o actualizado.
     */
    @Override
    public Subscription setItem(Subscription o) {
        return repository.save(o);
    }

    /**
     * Elimina una suscripción por su ID.
     *
     * Este método elimina una suscripción de la base de datos utilizando su ID. Si el ID es válido,
     * la suscripción será eliminada. Si no se encuentra la suscripción, se lanzará una excepción.
     *
     * @param id El ID de la suscripción a eliminar.
     */
    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica si existe una suscripción con el ID proporcionado.
     *
     * Este método verifica si existe una suscripción en la base de datos con el ID proporcionado.
     *
     * @param id El ID de la suscripción a verificar.
     * @return `true` si existe una suscripción con el ID dado, `false` si no existe.
     */
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<Subscription> getSubscriptionsByUserId(Long id) {
        return repository.getByUserId(id);
    }
}
