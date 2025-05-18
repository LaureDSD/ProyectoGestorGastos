package Proyecto.GestorAPI.config.security.oauth2;

/**
 * Enum que define los proveedores de OAuth2 soportados.
 */
public enum OAuth2Provider {

    LOCAL,   // Proveedor de autenticación local (es decir, no basado en OAuth2).
    GITHUB,  // Proveedor de autenticación a través de GitHub.
    GOOGLE   // Proveedor de autenticación a través de Google.
}
