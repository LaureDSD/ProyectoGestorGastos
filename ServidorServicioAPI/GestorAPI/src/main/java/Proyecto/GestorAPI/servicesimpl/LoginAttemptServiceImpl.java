package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.repositories.LoginAttemptRepository;
import Proyecto.GestorAPI.services.LoginAttemptService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    @Value("${attemp.login.max.failed}")
    private int maxFailedAttempts;

    @Value("${attemp.login.block.duration}")
    private int blockDuration;

    @Value("${attemp.login.delete.log}")
    private boolean dropLog;

    private final LoginAttemptRepository loginAttemptRepository;

    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
    }

    /**
     * Registra el intento de login.
     * Si "success" es true, podría incluso limpiar los registros para el usuario.
     *
     */
    @Transactional
    public boolean registerLoginAttempt(String username, boolean success) {
        Instant now = Instant.now();
        // Guarda el intento
        loginAttemptRepository.save(new LoginAttempt(username, now, success));
        // Limpia los registros viejos (de más de BLOCK_DURATION)
        Instant cutoff = now.minus(Duration.ofMinutes(blockDuration));

        if(dropLog){
            loginAttemptRepository.deleteOlderThan(cutoff);
        }
        return success;
    }

    /**
     * Consulta si el usuario está bloqueado, es decir, si tiene 5 o más intentos fallidos
     * en los últimos 30 minutos.
     */
    public boolean isBlocked(String username) {
        Instant now = Instant.now();
        Instant cutoff = now.minus(Duration.ofMinutes(blockDuration));
        long failedAttempts = loginAttemptRepository.countFailedAttempts(username, cutoff);
        return failedAttempts >= maxFailedAttempts;
    }


    public Duration timeUntilUnlock(String username) {
        Instant now = Instant.now();
        Instant cutoff = now.minus(Duration.ofMinutes(blockDuration));
        List<LoginAttempt> failedAttempts = loginAttemptRepository.findByUsernameAndSuccessIsFalseAndAttemptTimeAfter(username, cutoff);

        if (failedAttempts.size() < maxFailedAttempts) {
            return Duration.ZERO;
        }

        // Ordena por fecha y toma el más antiguo
        failedAttempts.sort(Comparator.comparing(LoginAttempt::getAttemptTime));
        Instant firstAttemptTime = failedAttempts.get(0).getAttemptTime();
        Instant unlockTime = firstAttemptTime.plus(Duration.ofMinutes(blockDuration));

        return Duration.between(now, unlockTime);
    }
}

