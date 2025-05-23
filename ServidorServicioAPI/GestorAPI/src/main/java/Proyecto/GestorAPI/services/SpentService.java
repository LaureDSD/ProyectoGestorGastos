package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Spent;

import java.util.List;
import java.util.Optional;

public interface SpentService {

    /**
     * Obtiene una lista de todos los registros de gastos.
     *
     * Este método devuelve todos los objetos de tipo `Spent` almacenados en el sistema.
     * Los registros pueden ser utilizados para mostrar un resumen o listado de todos los gastos realizados.
     *
     * @return Una lista de objetos `Spent` que representan los gastos registrados.
     */
    List<Spent> getAll();

    /**
     * Obtiene un gasto específico por su ID.
     *
     * Este método permite recuperar un registro de gasto a partir de su identificador único (ID).
     * Devuelve un `Optional` para manejar casos en los que el gasto no exista.
     *
     * @param id El ID del gasto que se desea obtener.
     * @return Un `Optional` que contiene el gasto si se encuentra, o está vacío si no se encuentra.
     */
    Optional<Spent> getByID(Long id);

    /**
     * Crea o actualiza un gasto en el sistema.
     *
     * Este método permite guardar un objeto `Spent` en la base de datos, ya sea creando un nuevo gasto
     * o actualizando uno existente si ya tiene un ID.
     *
     * @param o El objeto `Spent` que representa el gasto a guardar o actualizar.
     * @return El objeto `Spent` guardado o actualizado.
     */
    Spent setItem(Spent o);

    /**
     * Elimina un gasto del sistema por su ID.
     *
     * Este método permite eliminar un gasto existente de la base de datos utilizando su ID.
     *
     * @param id El ID del gasto que se desea eliminar.
     */
    void deleteByID(Long id);

    /**
     * Verifica si un gasto con el ID especificado existe en el sistema.
     *
     * Este método comprueba si existe un registro de gasto con el ID proporcionado.
     *
     * @param id El ID del gasto que se desea comprobar.
     * @return `true` si el gasto existe, `false` si no existe.
     */
    boolean existsById(Long id);

    /**
     * Obtiene todos los gastos asociados a un usuario dado por su ID.
     *
     * @param clienteId El ID del usuario para obtener sus gastos.
     * @return Lista de gastos asociados al usuario.
     */
    List<Spent> getSpentsByUserId(Long clienteId);

    /**
     * Cuenta el número total de gastos asociados a un usuario.
     *
     * @param userId El ID del usuario.
     * @return El número total de gastos del usuario.
     */
    long countSpentsByUserId(Long userId);

    /**
     * Obtiene el conteo total de gastos en el sistema.
     *
     * @return Número total de gastos registrados.
     */
    int getCountSpents();
}
