package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.services.AIChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio que implementa la comunicación con un servidor de IA externa para enviar mensajes
 * y recibir respuestas a través de una API REST.
 *
 * Esta implementación usa RestTemplate para realizar llamadas HTTP POST al endpoint configurado,
 * enviando un mensaje y recibiendo la respuesta de la IA en formato JSON.
 */
@Service
public class AIChatServiceImpl implements AIChatService {

    /**
     * Cliente RestTemplate para realizar llamadas HTTP.
     */
    private final RestTemplate restTemplate;

    /**
     * URL base del servidor Python que expone la API de IA, inyectada desde propiedades.
     */
    @Value("${python.server.url}")
    private String aiApiUrl;

    /**
     * API key para autorización en la API de IA, inyectada desde propiedades.
     */
    @Value("${python.server.apiKey}")
    private String apiKey;

    /**
     * Constructor por defecto que inicializa el RestTemplate.
     */
    public AIChatServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Envía un mensaje de texto a la API de IA y obtiene la respuesta generada.
     *
     * @param mensaje Texto que se desea enviar a la IA.
     * @return Respuesta textual proporcionada por la IA.
     * @throws RuntimeException si la respuesta HTTP no es exitosa.
     */
    @Override
    public String enviarMensaje(String mensaje) {
        // Construcción del endpoint completo para el envío de mensajes
        String endpoint = aiApiUrl + "/api/aichat";

        // Preparación de headers HTTP, incluyendo tipo de contenido y autorización Bearer
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Cuerpo de la petición en formato JSON con el mensaje a enviar
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", mensaje);

        // Creación de la entidad HTTP con headers y cuerpo para la solicitud POST
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // Realización de la llamada POST a la API externa y recepción de la respuesta
        ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, request, Map.class);

        // Validación del código de estado HTTP para determinar éxito
        if (response.getStatusCode().is2xxSuccessful()) {
            // Extracción de la respuesta bajo la clave "respuesta" del JSON recibido
            Object res = response.getBody().get("respuesta");
            // Retorna la respuesta como String o texto por defecto si es nulo
            return res != null ? res.toString() : "Sin respuesta";
        } else {
            // En caso de error HTTP, lanza excepción con código de estado
            throw new RuntimeException("Error al contactar IA: " + response.getStatusCode());
        }
    }
}
