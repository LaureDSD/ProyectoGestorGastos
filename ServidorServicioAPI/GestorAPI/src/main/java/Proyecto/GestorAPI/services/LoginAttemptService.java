package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.repositories.LoginAttemptRepository;
import jakarta.transaction.Transactional;

public interface LoginAttemptService {

    @Transactional
    boolean registerLoginAttempt(String username, boolean success);
}
