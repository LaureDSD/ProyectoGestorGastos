package Proyecto.GestorAPI.repositories;


import Proyecto.GestorAPI.models.FormContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<FormContacto,Long> {
}
