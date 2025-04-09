package Proyecto.GestorAPI.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Clase de configuración que encapsula las propiedades del token JWT.
 *
 * Esta clase se enlaza automáticamente con las propiedades definidas en el
 * archivo `application.properties` o `application.yml` mediante el prefijo `jwt`.
 *
 * Ejemplo en `application.yml`:
 *
 * ```yaml
 * jwt:
 *   secret: clave-secreta-super-segura
 *   expiration-ms: 3600000
 * ```
 *
 * Campos:
 * - `secret`: clave secreta usada para firmar y verificar los tokens JWT.
 * - `expirationMs`: duración del token en milisegundos antes de expirar.
 */
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {

    /**
     * Clave secreta para firmar los tokens JWT.
     */
    private String secret;

    /**
     * Tiempo de expiración del token en milisegundos.
     */
    private long expirationMs;
}
