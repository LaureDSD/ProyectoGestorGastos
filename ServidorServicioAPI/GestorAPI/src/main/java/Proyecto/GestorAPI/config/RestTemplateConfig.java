package Proyecto.GestorAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de RestTemplate para hacer solicitudes HTTP en la aplicación.
 *
 * Esta clase se encarga de crear y exponer un bean de tipo RestTemplate que puede ser inyectado
 * en otras partes de la aplicación para realizar solicitudes HTTP a servicios externos.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Crea un bean RestTemplate que se puede inyectar en otras clases de la aplicación.
     * RestTemplate es utilizado para enviar solicitudes HTTP, como GET, POST, PUT, DELETE, etc.
     *
     * @return Una nueva instancia de RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Crear y devolver una instancia de RestTemplate
    }
}
