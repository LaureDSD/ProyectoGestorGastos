package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.FormContacto;
import Proyecto.GestorAPI.repositories.ContactoRepository;
import Proyecto.GestorAPI.services.FormContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar formularios de contacto.
 *
 * Proporciona métodos para obtener, guardar, eliminar y verificar formularios
 * de contacto en la base de datos mediante el repositorio ContactoRepository.
 */
@Service
public class ContactoServiceImpl implements FormContactoService {

    /**
     * Repositorio JPA para acceso a datos de formularios de contacto.
     */
    @Autowired
    private ContactoRepository contactoRepository;

    /**
     * Obtiene todos los formularios de contacto almacenados.
     *
     * @return Lista completa de objetos FormContacto.
     */
    @Override
    public List<FormContacto> getAll() {
        return contactoRepository.findAll();
    }

    /**
     * Obtiene un formulario de contacto por su ID.
     *
     * @param id Identificador único del formulario.
     * @return Optional que contiene el formulario si existe, o vacío si no.
     */
    @Override
    public Optional<FormContacto> getByID(Long id) {
        return contactoRepository.findById(id);
    }

    /**
     * Guarda o actualiza un formulario de contacto en la base de datos.
     *
     * @param contacto Objeto FormContacto a guardar.
     * @return El objeto FormContacto guardado con posibles actualizaciones (como ID generado).
     */
    @Override
    public FormContacto setItem(FormContacto contacto) {
        return contactoRepository.save(contacto);
    }

    /**
     * Elimina un formulario de contacto por su ID.
     *
     * @param id Identificador único del formulario a eliminar.
     */
    @Override
    public void deleteByID(Long id) {
        contactoRepository.deleteById(id);
    }

    /**
     * Verifica si un formulario de contacto existe en base al ID dado.
     *
     * @param id Identificador único del formulario.
     * @return true si existe, false si no.
     */
    @Override
    public boolean existsById(Long id) {
        return contactoRepository.existsById(id);
    }
}
