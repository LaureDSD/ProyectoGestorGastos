package Proyecto.GestorAPI.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    /**
     * Configura CORS para permitir solicitudes de ciertos orígenes.
     *
     * @param allowedOrigins Lista de orígenes permitidos, configurada en el archivo de propiedades.
     * @return Una fuente de configuración de CORS.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource(@Value("${app.cors.allowed-origins}") List<String> allowedOrigins) {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite el uso de credenciales (cookies, autenticación HTTP, etc.)
        configuration.setAllowCredentials(true);

        // Establece los orígenes permitidos desde el archivo de propiedades.
        configuration.setAllowedOrigins(allowedOrigins);

        // Permite todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedMethod("*");

        // Permite todos los encabezados en las solicitudes.
        configuration.addAllowedHeader("*");

        // Registra la configuración de CORS en todos los endpoints (/**).
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
