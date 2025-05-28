package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "formularioContacto")  // corregido el nombre de tabla
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Correo electrónico de contacto.
     * Obligatorio, válido y con longitud máxima 150 caracteres.
     */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es válido")
    @Size(max = 150, message = "El correo no puede superar 150 caracteres")
    @Column(nullable = false, length = 150)
    private String correo;

    /**
     * Asunto del mensaje.
     * Obligatorio, longitud máxima 150 caracteres.
     */
    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 150, message = "El asunto no puede superar 150 caracteres")
    @Column(nullable = false, length = 150)
    private String asunto;

    /**
     * Mensaje del contacto.
     * Obligatorio, longitud máxima 2000 caracteres.
     */
    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 2000, message = "El mensaje no puede superar 2000 caracteres")
    @Column(nullable = false, length = 2000)
    private String mensaje;

    /**
     * Indica si el mensaje fue revisado.
     * No nulo, por defecto false.
     */
    @NotNull
    @Column(nullable = false)
    private Boolean revisado = false;

    /**
     * Fecha y hora de creación.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de última actualización.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
