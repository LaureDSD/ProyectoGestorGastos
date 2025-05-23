package Proyecto.GestorAPI.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración general de la aplicación para el manejo de formatos de fecha y rutas de recursos estáticos.
 *
 * Esta clase implementa {@link WebMvcConfigurer} para extender y personalizar el comportamiento por defecto de Spring MVC.
 *
 * - Define el formato estándar para las fechas utilizadas en la capa de presentación.
 * - Configura un manejador de recursos para servir archivos estáticos (como imágenes u otros documentos)
 *   desde el sistema de archivos del servidor.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registra un formateador de fechas para convertir automáticamente cadenas de texto
     * en objetos {@link java.util.Date} en los controladores, utilizando el patrón ISO `yyyy-MM-dd'T'HH:mm`.
     *
     * Este patrón permite manejar fechas con precisión hasta los minutos, por ejemplo: "2025-05-23T14:30".
     *
     * @param registry el registro de formateadores de Spring donde se agrega el formateador personalizado.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd'T'HH:mm"));
    }

    /**
     * Configura un manejador de recursos para permitir el acceso externo a archivos almacenados
     * en la carpeta local `C:/uploads/` mediante rutas accesibles a través de la URL `/uploads/**`.
     *
     * Esto es útil para exponer imágenes, documentos u otros archivos subidos por los usuarios.
     *
     * Ejemplo: una imagen guardada en `C:/uploads/foto.jpg` será accesible vía `http://<host>:<port>/uploads/foto.jpg`.
     *
     * @param registry el registro de manejadores de recursos donde se configura la ruta virtual y física.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/uploads/");
    }
}
