package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.repositories.LoginAttemptRepository;
import Proyecto.GestorAPI.services.LoginAttemptService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * Servicio para gestionar intentos de login de usuarios.
 *
 * Registra cada intento (exitoso o fallido), verifica bloqueo por
 * múltiples intentos fallidos en un intervalo de tiempo configurado,
 * y calcula el tiempo restante hasta desbloqueo.
 */
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    /**
     * Número máximo de intentos fallidos permitidos antes de bloquear al usuario.
     * Valor configurado en application.properties con clave "attemp.login.max.failed".
     */
    @Value("${attemp.login.max.failed}")
    private int maxFailedAttempts;

    /**
     * Duración (en minutos) del bloqueo tras alcanzar máximo de intentos fallidos.
     * Valor configurado en application.properties con clave "attemp.login.block.duration".
     */
    @Value("${attemp.login.block.duration}")
    private int blockDuration;

    /**
     * Indicador para borrar registros antiguos de intentos de login.
     * Valor configurado en application.properties con clave "attemp.login.delete.log".
     */
    @Value("${attemp.login.delete.log}")
    private boolean dropLog;

    /**
     * Repositorio para acceso a datos de intentos de login.
     */
    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    /**
     * Registra un intento de login para un usuario dado.
     *
     * Si el intento es exitoso, se puede optar por limpiar registros antiguos.
     * También elimina registros viejos según configuración.
     *
     * @param username Nombre de usuario.
     * @param success true si el intento fue exitoso, false si fallido.
     * @return Devuelve el valor del parámetro success.
     */
    @Transactional
    public boolean registerLoginAttempt(String username, boolean success) {
        Instant now = Instant.now();
        // Guarda el intento de login con timestamp actual y resultado
        loginAttemptRepository.save(new LoginAttempt(username, now, success));
        // Calcula la fecha límite para borrar registros antiguos
        Instant cutoff = now.minus(Duration.ofMinutes(blockDuration));

        if(dropLog){
            // Borra registros anteriores a cutoff si está habilitado
            loginAttemptRepository.deleteOlderThan(cutoff);
        }
        return success;
    }

    /**
     * Verifica si un usuario está bloqueado debido a intentos fallidos recientes.
     *
     * Consulta la cantidad de intentos fallidos desde hace "blockDuration" minutos.
     *
     * @param username Nombre del usuario a consultar.
     * @return true si el usuario tiene intentos fallidos >= maxFailedAttempts, false en caso contrario.
     */
    public boolean isBlocked(String username) {
        Instant now = Instant.now();
        Instant cutoff = now.minus(Duration.ofMinutes(blockDuration));
        long failedAttempts = loginAttemptRepository.countFailedAttempts(username, cutoff);
        return failedAttempts >= maxFailedAttempts;
    }

    /**
     * Calcula cuánto tiempo falta para que el bloqueo de un usuario finalice.
     *
     * Si no está bloqueado, retorna duración cero.
     * En caso de bloqueo, calcula el tiempo restante desde el intento fallido más antiguo
     * hasta que termine el período de bloqueo.
     *
     * @param username Nombre del usuario a consultar.
     * @return Duración restante para desbloqueo, o cero si no está bloqueado.
     */
    public Duration timeUntilUnlock(String username) {
        Instant now = Instant.now();
        Instant cutoff = now.minus(Duration.ofMinutes(blockDuration));
        List<LoginAttempt> failedAttempts = loginAttemptRepository.findByUsernameAndSuccessIsFalseAndAttemptTimeAfter(username, cutoff);

        if (failedAttempts.size() < maxFailedAttempts) {
            return Duration.ZERO;
        }

        // Ordena intentos fallidos por fecha, tomando el más antiguo
        failedAttempts.sort(Comparator.comparing(LoginAttempt::getAttemptTime));
        Instant firstAttemptTime = failedAttempts.get(0).getAttemptTime();
        Instant unlockTime = firstAttemptTime.plus(Duration.ofMinutes(blockDuration));

        return Duration.between(now, unlockTime);
    }

    /**
     * Obtiene todos los registros de intentos de login almacenados.
     *
     * @return Lista con todos los intentos.
     */
    @Override
    public List<LoginAttempt> getAll() {
        return loginAttemptRepository.findAll();
    }

    /**
     * Obtiene los intentos de login de un usuario específico por nombre.
     *
     * @param username Nombre del usuario.
     * @return Lista de intentos asociados al usuario.
     */
    @Override
    public List<LoginAttempt> getByUsernameOrEamil(String username) {
        return loginAttemptRepository.findByUsername(username);
    }

    /**
     * Guarda un intento de login (nuevo o actualizado) en la base de datos.
     *
     * @param loginAttempt Objeto LoginAttempt a guardar.
     */
    @Override
    public void setItem(LoginAttempt loginAttempt) {
        loginAttemptRepository.save(loginAttempt);
    }

    /**
     * Método stub para eliminar intentos anteriores a una fecha determinada.
     * Actualmente retorna 0 y no realiza acción.
     *
     * @param cutoffDate Fecha límite para eliminación.
     * @return Número de registros eliminados (actualmente 0).
     */
    public int deleteAttemptsOlderThan(Instant cutoffDate) {
        return 0;
    }
}
