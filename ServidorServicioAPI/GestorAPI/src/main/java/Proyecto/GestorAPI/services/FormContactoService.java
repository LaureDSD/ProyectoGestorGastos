package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.FormContacto;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de gestión de formularios de contacto.
 * Define las operaciones básicas CRUD para entidades FormContacto.
 */
public interface FormContactoService {

    /**
     * Obtiene la lista completa de formularios de contacto.
     *
     * @return Lista de objetos FormContacto.
     */
    List<FormContacto> getAll();

    /**
     * Obtiene un formulario de contacto específico por su ID.
     *
     * @param id Identificador único del formulario de contacto.
     * @return Optional conteniendo el FormContacto si existe, o vacío si no.
     */
    Optional<FormContacto> getByID(Long id);

    /**
     * Crea o actualiza un formulario de contacto.
     *
     * @param contacto Objeto FormContacto a guardar.
     * @return El objeto FormContacto guardado.
     */
    FormContacto setItem(FormContacto contacto);

    /**
     * Elimina un formulario de contacto por su ID.
     *
     * @param id Identificador único del formulario a eliminar.
     */
    void deleteByID(Long id);

    /**
     * Verifica si existe un formulario de contacto con el ID especificado.
     *
     * @param id Identificador único a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsById(Long id);
}
