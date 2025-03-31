package Proyecto.GestorAPI.models.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeración que representa los tipos de usuario en el sistema")
public enum TipoUsuario {

    @Schema(description = "Usuario con acceso básico al juego")
    ROLE_USUARIO("Usuario", "Acceso básico al juego"),

    @Schema(description = "Soporte técnico con permisos adicionales")
    ROLE_SOPORTE("Soporte", "Soporte técnico con permisos adicionales"),

    @Schema(description = "Administrador con control total del sistema")
    ROLE_ADMINISTRADOR("Administrador", "Control total del sistema");

    private final String nombre;
    private final String descripcion;

    TipoUsuario(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}