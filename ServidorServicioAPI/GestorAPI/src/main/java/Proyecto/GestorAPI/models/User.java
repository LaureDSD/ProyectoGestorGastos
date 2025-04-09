package Proyecto.GestorAPI.models;

import Proyecto.GestorAPI.security.RoleServer;
import Proyecto.GestorAPI.security.oauth2.OAuth2Provider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Nombre de usuario identificador
    private String username;

    //Contrasena
    private String password;

    //Nombre del usuario
    private String name;

    //Correo
    private String email;

    //Rol del usuario
    private RoleServer role;

    //Imagende perfil
    private String imageUrl;

    //Aviso de gasto mensual
    private double noticeExpense;

    //Posible control de evolucion con clase
    private double budgetMonthly;

    //Estado de la cuenta
    private boolean active;

    //Lista de gastos borrable al borrar usuario
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Spent> spentList;

    //Procdedencia creacion del usaurio
    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    //Identificador del proveedor
    private String providerId;

    //Fecha creacion
    private LocalDateTime createdAt;

    //Fecha de ultima modificacion
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public User(String username, String password, String name, String email, RoleServer role, String imageUrl, OAuth2Provider provider, String providerId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.providerId = providerId;
        this.active = true;
        this.budgetMonthly = 500;
        this.noticeExpense = 100;
    }

}
