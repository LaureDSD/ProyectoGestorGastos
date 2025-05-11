package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.repositories.LoginAttemptRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public interface LoginAttemptService {

    @Transactional
    boolean registerLoginAttempt(String username, boolean success);

    List<LoginAttempt> getAll();

    List<LoginAttempt> getByUsernameOrEamil(String username);

    void setItem(LoginAttempt loginAttempt);
}
