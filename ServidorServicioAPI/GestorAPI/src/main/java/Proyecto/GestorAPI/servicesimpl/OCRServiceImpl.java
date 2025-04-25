package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.services.OCRService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;

@Service // Anotación para que Spring registre la clase como un bean
public class OCRServiceImpl implements OCRService {

    @Value("${python.server.url}")
    private String pythonServerUrl; // URL del servidor Python que procesa OCR

    private final RestTemplate restTemplate;

    public OCRServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String sendFileForOCR(File file) throws IOException {
        // Crea un archivo Multipart
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file);

        // Crear una solicitud HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        // Enviar la solicitud
        ResponseEntity<String> response = restTemplate.exchange(
                pythonServerUrl + "/procesar", // Endpoint del servidor Python
                HttpMethod.POST,
                entity,
                String.class
        );

        // Procesa la respuesta JSON
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // El resultado del OCR en formato JSON
        } else {
            throw new IOException("Error al procesar OCR: " + response.getStatusCode());
        }
    }
}
