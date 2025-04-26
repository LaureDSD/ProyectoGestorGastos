package Proyecto.GestorAPI.models;

import Proyecto.GestorAPI.security.RoleServer;
import Proyecto.GestorAPI.security.oauth2.OAuth2Provider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad que representa a un usuario del sistema.
 *
 * Contiene información relevante para la autenticación, autorización
 * y configuración de perfil, además de la relación con los gastos realizados.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    /** Identificador único del usuario (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario utilizado para autenticación. */
    private String username;

    /** Contraseña cifrada del usuario. */
    private String password;

    /** Nombre completo del usuario. */
    private String name;

    /** Correo electrónico del usuario. */
    private String email;

    /** Rol del usuario dentro del sistema (admin, user). */
    private RoleServer role;

    /** URL de la imagen de perfil del usuario. */
    private String imageUrl;

    /** Indica si la cuenta del usuario está activa. */
    private boolean active;

    /**
     * Lista de gastos asociados al usuario.
     * Se eliminan en cascada si el usuario es eliminado.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Spent> spentList;

    /** Proveedor externo de autenticación (Google, GitHub, etc.). */
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    /** ID único proporcionado por el proveedor externo (OAuth2). */
    private String providerId;

    /** Fecha de creación del usuario. */
    @JsonIgnore
    private LocalDateTime createdAt;

    /** Fecha de la última modificación del usuario. */
    @JsonIgnore
    private LocalDateTime updatedAt;

    /**
     * Método de callback ejecutado antes de persistir la entidad.
     * Inicializa las fechas de creación y modificación.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Método de callback ejecutado antes de actualizar la entidad.
     * Actualiza la fecha de modificación.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor completo para inicialización de un nuevo usuario.
     *
     * @param username    Nombre de usuario
     * @param password    Contraseña cifrada
     * @param name        Nombre del usuario
     * @param email       Correo electrónico
     * @param role        Rol asignado
     * @param imageUrl    URL de la imagen de perfil
     * @param provider    Proveedor externo (OAuth2)
     * @param providerId  ID proporcionado por el proveedor
     */
    public User(String username, String password, String name, String email, RoleServer role,
                String imageUrl, OAuth2Provider provider, String providerId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.providerId = providerId;
        this.active = true;
    }
}
