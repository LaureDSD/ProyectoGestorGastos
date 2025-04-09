package Proyecto.GestorAPI.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * Configuración personalizada del ObjectMapper de Jackson para la aplicación.
 *
 * Este bean se utiliza en toda la aplicación para serializar y deserializar objetos JSON
 * con soporte para fechas en formato legible, compatibilidad con la API de fechas de Java 8
 * y una mayor tolerancia a cambios en las clases del modelo (por ejemplo, al ignorar propiedades desconocidas).
 */
@Configuration
public class ObjectMapperConfig {

    /**
     * Bean de configuración global del ObjectMapper para personalizar el comportamiento
     * de la conversión JSON <-> Java.
     *
     * @return instancia de ObjectMapper personalizada.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Ignora propiedades desconocidas al deserializar, evitando errores si cambian los modelos
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Añade soporte para la API de fechas de Java 8 (LocalDateTime, LocalDate, etc.)
        mapper.registerModule(new JavaTimeModule());

        // Deshabilita la escritura de fechas como timestamps numéricos
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Define el formato estándar para la serialización/deserialización de fechas
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return mapper;
    }
}
