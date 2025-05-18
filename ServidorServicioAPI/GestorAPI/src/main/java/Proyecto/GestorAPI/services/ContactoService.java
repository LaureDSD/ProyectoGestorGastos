package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Contacto;

import java.util.List;
import java.util.Optional;

public interface ContactoService {

    List<Contacto> getAll();

    Optional<Contacto> getByID(Long id);

    Contacto setItem(Contacto contacto);

    void deleteByID(Long id);

    boolean existsById(Long id);

}
