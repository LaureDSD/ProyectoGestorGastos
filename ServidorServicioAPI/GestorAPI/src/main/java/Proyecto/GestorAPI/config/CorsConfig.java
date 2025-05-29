/*package Proyecto.GestorAPI.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to enable and customize CORS (Cross-Origin Resource Sharing) settings.
 *
 * This configuration allows the backend Spring API to accept requests from the Angular frontend
 * running on "http://localhost:4200". It permits standard HTTP methods and any headers.
 *
 * The purpose is to enable cross-origin requests during development between the frontend and backend,
 * avoiding CORS errors when the frontend tries to consume the backend API.
 */
/*@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;
    /**
     * Override method to configure CORS mappings.
     *
     * @param registry the CorsRegistry to add CORS mappings to
     *
     * This configuration:
     * - Applies to all paths ("/**")
     * - Allows requests only from "http://localhost:4200"
     * - Supports HTTP methods: GET, POST, PUT, DELETE, OPTIONS
     * - Allows all headers from the client
     */
    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

}
*/