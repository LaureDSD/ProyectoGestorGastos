package Proyecto.GestorAPI.models.usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;


@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario en el sistema")
public class Usuario {

    //ID unico de cada usuario
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long usuario_id;

    //Imagen de perfil de cada usuario
    @Column(name = "imagen_perfil", length = 255)
    @Schema(description = "URL de la imagen de perfil del usuario", example = "img1.jpg")
    private String imagen_perfil;

    //Nombre publico de cada usuario
    @NotNull
    @Size(max = 100)
    @Column(name = "nombre_usuario_pub", nullable = false, length = 100)
    @Schema(description = "Nombre público del usuario", example = "Usuario1")
    private String nombre_usuario_pub;

    //nombre privado y unico de cada usuario
    @NotNull
    @Size(max = 100)
    @Column(name = "nombre_usuario_priv", nullable = false, unique = true, length = 100)
    @Schema(description = "Nombre privado del usuario (para login)", example = "user1")
    private String nombre_usuario_priv;

    //Correo de cada usuario
    @NotNull
    @Email
    @Size(max = 100)
    @Column(name = "correo", nullable = false, unique = true, length = 100)
    @Schema(description = "Correo electrónico del usuario", example = "user1@example.com")
    private String correo;

    //Contrasena de cada usuario
    @NotNull
    @Column(name = "contraseña", nullable = false, length = 255)
    @Schema(description = "Contraseña del usuario", example = "password1")
    private String contraseña;

    //Ultima conexion
    @NotNull
    @DateTimeFormat
    @Column(name = "ultima_conexion")
    @Schema(description = "Fecha y hora de la última conexión del usuario", example = "2023-10-01T12:00:00")
    private LocalDateTime ultima_conexion;

    //Fecha en la que se crea el usuario
    @NotNull
    @DateTimeFormat
    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha y hora de creación del usuario", example = "2023-10-01T12:00:00")
    private LocalDateTime fecha_creacion = LocalDateTime.now();

    //Estado de la cuenta sin ser borrada, activa o inactiva
    @NotNull
    @Column(name = "estado_cuenta", nullable = false)
    @Schema(description = "Estado de la cuenta del usuario (activa/inactiva)", example = "true")
    private boolean estado_cuenta;

    //Tipo de usuario
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    public Usuario() {
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isEstado_cuenta() {
        return estado_cuenta;
    }

    public void setEstado_cuenta(boolean estado_cuenta) {
        this.estado_cuenta = estado_cuenta;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getImagen_perfil() {
        return imagen_perfil;
    }

    public void setImagen_perfil(String imagen_perfil) {
        this.imagen_perfil = imagen_perfil;
    }

    public String getNombre_usuario_priv() {
        return nombre_usuario_priv;
    }

    public void setNombre_usuario_priv(String nombre_usuario_priv) {
        this.nombre_usuario_priv = nombre_usuario_priv;
    }

    public String getNombre_usuario_pub() {
        return nombre_usuario_pub;
    }

    public void setNombre_usuario_pub(String nombre_usuario_pub) {
        this.nombre_usuario_pub = nombre_usuario_pub;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public LocalDateTime getUltima_conexion() {
        return ultima_conexion;
    }

    public void setUltima_conexion(LocalDateTime ultima_conexion) {
        this.ultima_conexion = ultima_conexion;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }
}