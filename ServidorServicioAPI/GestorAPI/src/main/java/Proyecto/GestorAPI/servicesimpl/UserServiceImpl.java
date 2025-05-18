package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.exceptions.UserNotFoundException;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.repositories.UserRepository;
import Proyecto.GestorAPI.config.security.oauth2.OAuth2Provider;
import Proyecto.GestorAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static Proyecto.GestorAPI.controllers.securityController.MySecurityrController.isEmail;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    // Inyección del repositorio de User para interactuar con la base de datos.
    private final UserRepository userRepository;

    /**
     * Obtiene todos los usuarios registrados.
     *
     * Este método consulta la base de datos para obtener una lista de todos los usuarios.
     *
     * @return Una lista de todos los usuarios registrados en la base de datos.
     */
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * Este método busca un usuario específico a partir de su ID. Devuelve un `Optional` que contiene
     * el objeto `User` si se encuentra, o un `Optional.empty()` si no se encuentra.
     *
     * @param userId El ID del usuario a buscar.
     * @return Un `Optional` que contiene el usuario si se encuentra, o un `Optional.empty()` si no se encuentra.
     */
    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * Este método busca un usuario específico a partir de su nombre de usuario. Devuelve un `Optional`
     * que contiene el objeto `User` si se encuentra, o un `Optional.empty()` si no se encuentra.
     *
     * @param username El nombre de usuario a buscar.
     * @return Un `Optional` que contiene el usuario si se encuentra, o un `Optional.empty()` si no se encuentra.
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * Este método busca un usuario específico a partir de su correo electrónico. Devuelve un `Optional`
     * que contiene el objeto `User` si se encuentra, o un `Optional.empty()` si no se encuentra.
     *
     * @param email El correo electrónico a buscar.
     * @return Un `Optional` que contiene el usuario si se encuentra, o un `Optional.empty()` si no se encuentra.
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Guarda un nuevo usuario o actualiza uno existente.
     *
     * Este método guarda un nuevo usuario en la base de datos o actualiza un usuario existente si el
     * objeto `User` ya existe.
     *
     * @param user El objeto `User` a guardar o actualizar.
     * @return El objeto `User` guardado o actualizado.
     */
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Elimina un usuario.
     *
     * Este método elimina un usuario de la base de datos.
     *
     * @param user El objeto `User` a eliminar.
     */
    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     *
     * Este método verifica si existe un usuario con el nombre de usuario especificado.
     *
     * @param username El nombre de usuario a verificar.
     * @return `true` si existe un usuario con el nombre de usuario dado, `false` si no existe.
     */
    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Verifica si existe un usuario con el correo electrónico proporcionado.
     *
     * Este método verifica si existe un usuario con el correo electrónico especificado.
     *
     * @param email El correo electrónico a verificar.
     * @return `true` si existe un usuario con el correo electrónico dado, `false` si no existe.
     */
    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Valida y obtiene un usuario por su nombre de usuario.
     *
     * Este método busca un usuario por su nombre de usuario y lanza una excepción `UserNotFoundException`
     * si no se encuentra el usuario. Si el usuario es encontrado, se devuelve el objeto `User`.
     *
     * @param username El nombre de usuario a buscar y validar.
     * @return El objeto `User` si se encuentra el usuario.
     * @throws UserNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     */
    @Override
    public User validateAndGetUserByUsername(String username) {
        return getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username %s not found", username)));
    }

    /**
     * Valida y obtiene un usuario por su correo electrónico.
     *
     * Este método busca un usuario por su correo electrónico y lanza una excepción `UserNotFoundException`
     * si no se encuentra el usuario. Si el usuario es encontrado, se devuelve el objeto `User`.
     *
     * @param email El correo electrónico a buscar y validar.
     * @return El objeto `User` si se encuentra el usuario.
     * @throws UserNotFoundException Si no se encuentra un usuario con el correo electrónico proporcionado.
     */
    @Override
    public User validateAndGetUserByUsermail(String email) {
        return getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", email)));
    }

    /**
     * Método que busca un usuario en base al nombre de usuario o al correo electrónico.
     *
     * Este método primero verifica si el parámetro `user` tiene el formato de un correo electrónico.
     * Si es así, intenta buscar el usuario por correo electrónico. Si no es un correo electrónico,
     * lo considera como un nombre de usuario y busca al usuario usando el nombre de usuario.
     *
     * @param user El nombre de usuario o correo electrónico del usuario a buscar.
     * @return Un `Optional<User>` que contiene el usuario si se encuentra, o un valor vacío si no se encuentra.
     */
    @Override
    public Optional<User> getUserByUsernameOrEmail(String user) {
        if (isEmail(user)) {
            return getUserByEmail(user);
        } else {
            return getUserByUsername(user);
        }
    }

    @Override
    public long countByProvider(OAuth2Provider oAuth2Provider) {
        return userRepository.countByProvider(oAuth2Provider);
    }
}
