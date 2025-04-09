package Proyecto.GestorAPI.config;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Configuración personalizada para el manejo de errores en la API.
 *
 * Esta clase sobrescribe los atributos por defecto del error para incluir
 * información adicional en las respuestas de error del servidor, como:
 * - El tipo de excepción lanzada.
 * - El mensaje de error.
 * - Los errores de validación (binding).
 *
 * Esto resulta útil para depurar y proporcionar mejores respuestas al cliente.
 */
@Configuration
public class ErrorAttributesConfig {

    /**
     * Define un bean personalizado de {@link ErrorAttributes} para sobrescribir
     * el comportamiento por defecto y mostrar más detalles en los errores HTTP.
     *
     * @return objeto personalizado de ErrorAttributes
     */
    @Bean
    ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                // Incluye detalles adicionales como el mensaje, la excepción y errores de binding
                return super.getErrorAttributes(
                        webRequest,
                        options.including(
                                Include.EXCEPTION,
                                Include.MESSAGE,
                                Include.BINDING_ERRORS
                        )
                );
            }
        };
    }
}
