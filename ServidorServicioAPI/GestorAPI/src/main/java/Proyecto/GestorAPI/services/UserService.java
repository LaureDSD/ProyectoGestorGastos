package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getUsers();

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    User validateAndGetUserByUsername(String username);

    User validateAndGetUserByUsermail(String email);

    User saveUser(User user);

    void deleteUser(User user);

    Optional<User> getUserById(Long id);
}
