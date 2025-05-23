package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Entity
@Table(name = "registroLogin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario que intenta el login.
     * Obligatorio, no vacío, máximo 100 caracteres.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 100, message = "El nombre de usuario no puede superar 100 caracteres")
    @Column(nullable = false, length = 100)
    private String username;

    /**
     * Fecha y hora del intento de login.
     * Obligatorio.
     */
    @NotNull(message = "La fecha y hora del intento es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private Instant attemptTime;

    /**
     * Indica si el intento fue exitoso o no.
     * Obligatorio.
     */
    @NotNull(message = "El estado de éxito es obligatorio")
    @Column(nullable = false)
    private Boolean success;

}
