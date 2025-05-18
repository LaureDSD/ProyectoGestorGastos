package Proyecto.GestorAPI.config.security;

/**
 * Enumeración que define los roles de usuario en el sistema.
 * Se utiliza para asignar diferentes niveles de acceso a las funcionalidades
 * de la aplicación, basándose en el rol de cada usuario.
 *
 * Los roles definidos son:
 * - ADMIN: Rol de administrador, con privilegios completos sobre la aplicación.
 * - USER: Rol de usuario estándar, con acceso limitado a las funcionalidades.
 */
public enum RoleServer {
    ADMIN, // Rol de administrador, con privilegios elevados y capacidad para gestionar el sistema.
    USER   // Rol de usuario, con privilegios restringidos a las operaciones básicas del sistema.
}
