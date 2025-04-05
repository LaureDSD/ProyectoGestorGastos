package Proyecto.GestorAPI.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes(BEARER_KEY_SECURITY_SCHEME,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                )
                .info(new Info()
                        .title("GesThor - Gestión de Gastos API")
                        .description("API para la gestión de gastos personales y de empresa. Permite el registro de tickets, productos, usuarios .")
                        .version("1.0")
                        .termsOfService("https://www.example.com/terms")
                        .contact(new Info().contact(new Contact().name("Soporte GesThor").url("https://www.example.com/support").email("soporte@gesthor.com")).getContact())
                        .license(new io.swagger.v3.oas.models.info.License().name("MIT License").url("https://opensource.org/licenses/MIT"))
                );
    }

    public static final String BEARER_KEY_SECURITY_SCHEME = "bearer-key";
}
