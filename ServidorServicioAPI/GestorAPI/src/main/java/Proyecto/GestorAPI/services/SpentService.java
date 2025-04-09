package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Spent;

import java.util.List;
import java.util.Optional;

public interface SpentService {
    List<Spent> getAll();

    Optional<Spent> getByID(Long id);

    Spent setItem(Spent o);

    void deleteByID(Long id);

    boolean existsById(Long id);
}
