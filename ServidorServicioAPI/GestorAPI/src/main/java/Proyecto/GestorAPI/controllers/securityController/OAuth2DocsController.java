package Proyecto.GestorAPI.controllers.securityController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de documentación para exponer en Swagger las rutas
 * de inicio y callback de OAuth2 (Google y GitHub).
 */
@RestController
@Tag(name = "OAuth2 (Open)", description = "Documentación de endpoints OAuth2")
public class OAuth2DocsController {

    @Operation(summary = "Iniciar login OAuth2 con Google",
            description = "Redirige al usuario a la página de autorización de Google")
    @GetMapping("/oauth2/authorization/google")
    public void oauth2GoogleAuthorize() {
        // Solo para Swagger. Spring Security intercepta esta ruta.
    }

    @Operation(summary = "Callback OAuth2 Google",
            description = "Punto de entrada para que Google devuelva el código de autorización")
    @GetMapping("/login/oauth2/code/google")
    public void oauth2GoogleCallback() {
        // Solo para Swagger. Spring Security gestiona internamente este endpoint.
    }

    @Operation(summary = "Iniciar login OAuth2 con GitHub",
            description = "Redirige al usuario a la página de autorización de GitHub")
    @GetMapping("/oauth2/authorization/github")
    public void oauth2GithubAuthorize() {
        // Solo para Swagger.
    }

    @Operation(summary = "Callback OAuth2 GitHub",
            description = "Punto de entrada para que GitHub devuelva el código de autorización")
    @GetMapping("/login/oauth2/code/github")
    public void oauth2GithubCallback() {
        // Solo para Swagger.
    }
}
