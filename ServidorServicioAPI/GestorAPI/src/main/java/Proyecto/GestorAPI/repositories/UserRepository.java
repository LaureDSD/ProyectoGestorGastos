package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.security.oauth2.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para acceder y manipular los datos de los usuarios (User).
 * Esta interfaz extiende JpaRepository para facilitar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la entidad User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario (username).
     *
     * @param username el nombre de usuario para buscar.
     * @return un Optional que contiene el usuario si se encuentra, o está vacío si no se encuentra.
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por su correo electrónico (email).
     *
     * @param email el correo electrónico para buscar.
     * @return un Optional que contiene el usuario si se encuentra, o está vacío si no se encuentra.
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si un usuario con el nombre de usuario proporcionado ya existe en la base de datos.
     *
     * @param username el nombre de usuario para verificar.
     * @return true si existe un usuario con ese nombre de usuario, false en caso contrario.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si un usuario con el correo electrónico proporcionado ya existe en la base de datos.
     *
     * @param email el correo electrónico para verificar.
     * @return true si existe un usuario con ese correo electrónico, false en caso contrario.
     */
    boolean existsByEmail(String email);

    long countByProvider(OAuth2Provider oAuth2Provider);
}
