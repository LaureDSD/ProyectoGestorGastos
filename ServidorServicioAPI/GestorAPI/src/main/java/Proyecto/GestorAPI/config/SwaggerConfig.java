package Proyecto.GestorAPI.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API de GesThor.
 * Esta configuración incluye la definición del esquema de seguridad (JWT Bearer)
 * y la información básica de la API (nombre, descripción, contacto, licencia, etc.).
 * Swagger permitirá a los desarrolladores interactuar y consultar la documentación de la API de manera sencilla.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Inyecta el nombre de la aplicación desde el archivo de configuración (application.properties o application.yml).
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * Configuración de la API para Swagger/OpenAPI.
     * Define la información básica de la API, incluyendo el esquema de seguridad para autenticación con JWT.
     *
     * @return Un objeto OpenAPI configurado con la información y los esquemas de seguridad de la API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Configura los esquemas de seguridad (Bearer Token JWT en este caso)
                .components(
                        new Components().addSecuritySchemes(BEARER_KEY_SECURITY_SCHEME,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP) // Tipo de seguridad HTTP
                                        .scheme("bearer") // Indica que se utilizará un token de tipo "bearer"
                                        .bearerFormat("JWT")) // Formato del token: JWT
                )
                // Información general sobre la API
                .info(new Info()
                        .title("GesThor - Gestión de Gastos API") // Título de la API
                        .description("API para la gestión de gastos personales y de empresa. Permite el registro de tickets, productos, usuarios.") // Descripción detallada
                        .version("1.0") // Versión de la API
                        .termsOfService("https://www.gesthor.com/terms") // URL de los términos de servicio
                        .contact(new Info().contact(new Contact().name("Soporte GesThor") // Información de contacto
                                        .url("https://www.gesthor.com/support") // URL del soporte
                                        .email("soporte@gesthor.com")) // Correo electrónico de soporte
                                .getContact())
                        // Licencia de uso de la API
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("MIT License") // Licencia utilizada para la API
                                .url("https://opensource.org/licenses/MIT")) // URL de la licencia
                );
    }

    /**
     * Nombre del esquema de seguridad utilizado para la autenticación mediante token JWT.
     * Este valor será usado para indicar que la API utiliza tokens Bearer para autenticación.
     */
    public static final String BEARER_KEY_SECURITY_SCHEME = "bearer-key";
}
