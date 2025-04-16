package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    // Obtiene los intentos fallidos dentro de un período para un usuario
    @Query("SELECT COUNT(la) FROM LoginAttempt la WHERE la.username = :username AND la.success = false AND la.attemptTime >= :since")
    long countFailedAttempts(@Param("username") String username, @Param("since") Instant since);

    // Elimina los intentos cuya fecha es anterior al tiempo especificado
    @Modifying
    @Query("DELETE FROM LoginAttempt la WHERE la.attemptTime < :olderThan")
    void deleteOlderThan(@Param("olderThan") Instant olderThan);

    List<LoginAttempt> findByUsernameAndSuccessIsFalseAndAttemptTimeAfter(String username, Instant cutoff);

}
