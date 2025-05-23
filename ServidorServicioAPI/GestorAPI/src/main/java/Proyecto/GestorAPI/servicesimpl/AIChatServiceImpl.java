package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.services.AIChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIChatServiceImpl implements AIChatService {

    private final RestTemplate restTemplate;

    @Value("${python.server.url}")
    private String aiApiUrl;

    @Value("${python.server.apiKey}")
    private String apiKey;

    public AIChatServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String enviarMensaje(String mensaje) {
        String endpoint = aiApiUrl + "/api/aichat";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, String> body = new HashMap<>();
        body.put("mensaje", mensaje);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(endpoint, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Object res = response.getBody().get("respuesta");
            return res != null ? res.toString() : "Sin respuesta";
        } else {
            throw new RuntimeException("Error al contactar IA: " + response.getStatusCode());
        }
    }
}
