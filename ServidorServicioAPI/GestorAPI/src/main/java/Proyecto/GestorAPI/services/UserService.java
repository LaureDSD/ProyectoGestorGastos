package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.config.security.oauth2.OAuth2Provider;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * Este método devuelve una lista de todos los usuarios almacenados en la base de datos.
     * Puede ser útil para mostrar un listado de usuarios en la plataforma.
     *
     * @return Una lista de objetos `User` que representan todos los usuarios registrados.
     */
    List<User> getUsers();

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * Este método permite obtener un usuario específico a partir de su nombre de usuario.
     * Devuelve un `Optional` para manejar el caso en que no exista un usuario con ese nombre.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return Un `Optional` que contiene el usuario si se encuentra, o está vacío si no se encuentra.
     */
    Optional<User> getUserByUsername(String username);

    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * Este método permite obtener un usuario a partir de su correo electrónico.
     * Devuelve un `Optional` para manejar el caso en que no exista un usuario con ese correo electrónico.
     *
     * @param email El correo electrónico del usuario a buscar.
     * @return Un `Optional` que contiene el usuario si se encuentra, o está vacío si no se encuentra.
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado.
     *
     * Este método verifica si hay algún usuario registrado con el nombre de usuario especificado.
     *
     * @param username El nombre de usuario a verificar.
     * @return `true` si existe un usuario con el nombre de usuario, `false` si no existe.
     */
    boolean hasUserWithUsername(String username);

    /**
     * Verifica si existe un usuario con el correo electrónico proporcionado.
     *
     * Este método verifica si hay algún usuario registrado con el correo electrónico especificado.
     *
     * @param email El correo electrónico a verificar.
     * @return `true` si existe un usuario con el correo electrónico, `false` si no existe.
     */
    boolean hasUserWithEmail(String email);

    /**
     * Valida y obtiene un usuario por su nombre de usuario.
     *
     * Este método busca un usuario por su nombre de usuario. Si no se encuentra, lanza una excepción.
     * Este método se utiliza cuando es necesario garantizar que el usuario existe y es válido.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return El objeto `User` que corresponde al nombre de usuario.
     * @throws RuntimeException si no se encuentra un usuario con el nombre de usuario.
     */
    User validateAndGetUserByUsername(String username);

    /**
     * Valida y obtiene un usuario por su correo electrónico.
     *
     * Este método busca un usuario por su correo electrónico. Si no se encuentra, lanza una excepción.
     * Este método se utiliza cuando es necesario garantizar que el usuario existe y es válido.
     *
     * @param email El correo electrónico del usuario a buscar.
     * @return El objeto `User` que corresponde al correo electrónico.
     * @throws RuntimeException si no se encuentra un usuario con el correo electrónico.
     */
    User validateAndGetUserByUsermail(String email);

    /**
     * Guarda un nuevo usuario o actualiza un usuario existente.
     *
     * Este método permite guardar un nuevo usuario en la base de datos o actualizar un usuario existente.
     *
     * @param user El objeto `User` que se desea guardar o actualizar.
     * @return El usuario guardado o actualizado.
     */
    User saveUser(User user);

    /**
     * Elimina un usuario del sistema.
     *
     * Este método elimina el usuario proporcionado del sistema.
     *
     * @param user El objeto `User` que se desea eliminar.
     */
    void deleteUser(User user);

    /**
     * Obtiene un usuario por su ID.
     *
     * Este método permite obtener un usuario específico utilizando su ID único.
     *
     * @param id El ID del usuario a obtener.
     * @return Un `Optional` que contiene el usuario si se encuentra, o está vacío si no se encuentra.
     */
    Optional<User> getUserById(Long id);



    /**
     * Método para obtener un usuario por su nombre de usuario o correo electrónico.
     *
     * Este método permite buscar un usuario en la base de datos utilizando un único parámetro,
     * que puede ser un nombre de usuario o un correo electrónico. La búsqueda se realiza de manera
     * flexible, permitiendo que el mismo método gestione ambos casos.
     *
     * @param user El nombre de usuario o el correo electrónico del usuario a buscar.
     * @return Un `Optional<User>` que contiene el usuario encontrado, o un valor vacío si no se encuentra.
     */
    Optional<User> getUserByUsernameOrEmail(String user);

    long countByProvider(OAuth2Provider oAuth2Provider);

    int getCountUsers();
}
