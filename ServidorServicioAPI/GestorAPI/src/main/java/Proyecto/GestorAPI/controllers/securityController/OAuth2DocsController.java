package Proyecto.GestorAPI.controllers.securityController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador auxiliar para documentar en Swagger los endpoints
 * utilizados en el flujo de autenticación OAuth2 (Google y GitHub).
 *
 * ⚠️ Importante: Estas rutas son interceptadas y gestionadas por Spring Security,
 * por lo que este controlador no contiene lógica de negocio ni procesamiento real.
 * Su única función es permitir la visibilidad de estos endpoints en Swagger UI.
 */
@RestController
@Tag(name = "OAuth2 (Open)", description = "Documentación de endpoints OAuth2")
public class OAuth2DocsController {

    /**
     * Endpoint de inicio de autenticación OAuth2 con Google.
     *
     * Redirige automáticamente al usuario hacia la página de autorización de Google.
     * No requiere token ni autenticación previa.
     */
    @Operation(
            summary = "Iniciar login OAuth2 con Google",
            description = "Redirige al usuario a la página de autorización de Google. " +
                    "Este endpoint es gestionado por Spring Security y no debe invocarse manualmente."
    )
    @GetMapping("/oauth2/authorization/google")
    public void oauth2GoogleAuthorize() {
        // Ruta manejada por Spring Security para inicio de login con Google.
    }

    /**
     * Callback de OAuth2 para Google.
     *
     * Este endpoint recibe el código de autorización tras el login exitoso con Google.
     */
    @Operation(
            summary = "Callback OAuth2 Google",
            description = "Punto de entrada gestionado por Spring Security para completar el flujo OAuth2 con Google. " +
                    "El usuario es redirigido aquí automáticamente por Google tras iniciar sesión."
    )
    @GetMapping("/login/oauth2/code/google")
    public void oauth2GoogleCallback() {
        // Ruta manejada por Spring Security como callback de Google.
    }

    /**
     * Endpoint de inicio de autenticación OAuth2 con GitHub.
     *
     * Redirige al usuario a la página de autorización de GitHub.
     */
    @Operation(
            summary = "Iniciar login OAuth2 con GitHub",
            description = "Redirige al usuario a la página de autorización de GitHub. " +
                    "Este endpoint es gestionado automáticamente por Spring Security."
    )
    @GetMapping("/oauth2/authorization/github")
    public void oauth2GithubAuthorize() {
        // Ruta manejada por Spring Security para inicio de login con GitHub.
    }

    /**
     * Callback de OAuth2 para GitHub.
     *
     * Este endpoint recibe el código de autorización tras el login exitoso con GitHub.
     */
    @Operation(
            summary = "Callback OAuth2 GitHub",
            description = "Punto de entrada gestionado por Spring Security para completar el flujo OAuth2 con GitHub. " +
                    "GitHub redirige al usuario a esta URL tras la autenticación."
    )
    @GetMapping("/login/oauth2/code/github")
    public void oauth2GithubCallback() {
        // Ruta manejada por Spring Security como callback de GitHub.
    }
}
