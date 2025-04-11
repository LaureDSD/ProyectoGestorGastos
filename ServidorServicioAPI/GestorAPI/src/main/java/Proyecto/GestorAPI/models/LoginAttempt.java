package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "registroLogin")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Instant attemptTime;

    private boolean success;

    public LoginAttempt(String username, Instant attemptTime, boolean success) {
        this.username = username;
        this.attemptTime = attemptTime;
        this.success = success;
    }

}
