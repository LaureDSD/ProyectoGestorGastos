package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.Spent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpentRepository extends JpaRepository<Spent,Long> {
}
