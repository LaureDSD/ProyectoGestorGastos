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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/server")
public class ServerInfoController {

    @Autowired
    private ServerStatsServiceImpl serverStatsService;

    @GetMapping("/info")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener Informacion Servidor"
    )
    public ServerInfoDto getServerInfo() {
        return serverStatsService.getFullServerInfo();
    }
}
