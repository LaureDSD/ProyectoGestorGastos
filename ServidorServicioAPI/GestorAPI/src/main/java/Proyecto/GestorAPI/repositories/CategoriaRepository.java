package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository< Categoria, Long> {
}
