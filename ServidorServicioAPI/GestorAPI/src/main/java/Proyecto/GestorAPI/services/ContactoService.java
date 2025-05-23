package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.FormContacto;

import java.util.List;
import java.util.Optional;

public interface ContactoService {

    List<FormContacto> getAll();

    Optional<FormContacto> getByID(Long id);

    FormContacto setItem(FormContacto contacto);

    void deleteByID(Long id);

    boolean existsById(Long id);

}
