package Proyecto.GestorAPI.controllers.securityController;

import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador REST para administración de intentos de inicio de sesión.
 *
 * Proporciona un endpoint para consultar el historial de intentos de login de usuarios.
 * Solo accesible para administradores autenticados.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/loginAttemps")
@Tag(name = "Login Log (Admin only)", description = "Lista de intentos de acceso")
public class LoginAttempController {

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    /**
     * Retorna una lista de intentos de inicio de sesión, filtrando opcionalmente por nombre de usuario o correo.
     *
     * Este endpoint requiere autenticación por token Bearer y está restringido a usuarios administradores.
     *
     * @param user Nombre de usuario o correo electrónico para filtrar los intentos de acceso (opcional).
     * @return Lista de intentos de login relacionados con el usuario indicado, o todos si no se filtra.
     */
    @GetMapping("/")
    @Operation(
            summary = "Obtener intentos de acceso",
            description = "Devuelve todos los intentos de login registrados, o solo los de un usuario específico si se indica un filtro.",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Intentos de acceso encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay intentos de acceso registrados"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o ausente"),
            @ApiResponse(responseCode = "403", description = "Prohibido - No tiene permisos para acceder a este recurso")
    })
    public ResponseEntity<List<LoginAttempt>> getLogins(
            @Parameter(
                    description = "Nombre de usuario o email para filtrar los intentos de login",
                    required = false,
                    example = "admin@example.com"
            )
            @RequestParam(value = "username/email", required = false) String user) {

        List<LoginAttempt> loginAttempts;
        if (user != null) {
            loginAttempts = loginAttemptService.getByUsernameOrEamil(user);
        } else {
            loginAttempts = loginAttemptService.getAll();
        }

        if (loginAttempts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(loginAttempts);
    }
}
