package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    /**
     * Obtiene una lista de todas las suscripciones.
     *
     * Este método devuelve todos los objetos de tipo `Subscription` almacenados en el sistema.
     * Los registros de suscripciones pueden ser utilizados para mostrar un listado o resumen
     * de todas las suscripciones activas o pasadas.
     *
     * @return Una lista de objetos `Subscription` que representan las suscripciones registradas.
     */
    List<Subscription> getAll();

    /**
     * Obtiene una suscripción específica por su ID.
     *
     * Este método permite recuperar un registro de suscripción a partir de su identificador único (ID).
     * Devuelve un `Optional` para manejar casos en los que la suscripción no exista.
     *
     * @param id El ID de la suscripción que se desea obtener.
     * @return Un `Optional` que contiene la suscripción si se encuentra, o está vacío si no se encuentra.
     */
    Optional<Subscription> getByID(Long id);

    /**
     * Crea o actualiza una suscripción en el sistema.
     *
     * Este método permite guardar un objeto `Subscription` en la base de datos, ya sea creando una nueva suscripción
     * o actualizando una existente si ya tiene un ID.
     *
     * @param o El objeto `Subscription` que representa la suscripción a guardar o actualizar.
     * @return El objeto `Subscription` guardado o actualizado.
     */
    Subscription setItem(Subscription o);

    /**
     * Elimina una suscripción del sistema por su ID.
     *
     * Este método permite eliminar una suscripción existente de la base de datos utilizando su ID.
     *
     * @param id El ID de la suscripción que se desea eliminar.
     */
    void deleteByID(Long id);

    /**
     * Verifica si una suscripción con el ID especificado existe en el sistema.
     *
     * Este método comprueba si existe un registro de suscripción con el ID proporcionado.
     *
     * @param id El ID de la suscripción que se desea comprobar.
     * @return `true` si la suscripción existe, `false` si no existe.
     */
    boolean existsById(Long id);
}
