package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.usuario.TipoUsuario;

import java.time.LocalDateTime;
import java.util.Date;

public class UsuarioDTO {
    private Long id;
    private String imagen;
    private String nombrePublico;
    private String nombrePrivado;
    private String correo;
    private String contrasena;
    private LocalDateTime conexion;
    private LocalDateTime fecha_creacion;
    private boolean estado;
    private TipoUsuario tipoUsuario;

    public UsuarioDTO() {
    }

    public LocalDateTime getConexion() {
        return conexion;
    }

    public void setConexion(LocalDateTime conexion) {
        this.conexion = conexion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombrePrivado() {
        return nombrePrivado;
    }

    public void setNombrePrivado(String nombrePrivado) {
        this.nombrePrivado = nombrePrivado;
    }

    public String getNombrePublico() {
        return nombrePublico;
    }

    public void setNombrePublico(String nombrePublico) {
        this.nombrePublico = nombrePublico;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
