package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.modelsDTO.ServerInfoDto;
import Proyecto.GestorAPI.servicesimpl.ServerStatsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador para obtener información del servidor.
 *
 * Proporciona un endpoint protegido para recuperar datos detallados
 * sobre el estado y estadísticas del servidor donde corre la aplicación.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/server")
public class ServerInfoController {

    @Autowired
    private ServerStatsServiceImpl serverStatsService;

    /**
     * Endpoint para obtener información completa del servidor.
     *
     * Requiere autenticación con token Bearer.
     *
     * @return DTO con información detallada del servidor.
     */
    @GetMapping("/info")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener Información del Servidor"
    )
    public ServerInfoDto getServerInfo() {
        return serverStatsService.getFullServerInfo();
    }
}
