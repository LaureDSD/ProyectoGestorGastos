package Proyecto.GestorAPI.config;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

public class PythonSimulatorApp {

    public static void main(String[] args) {
        // Simula la creación de un archivo temporal
        File file = new File("sample_ticket.jpg"); // Cambia a un archivo válido para las pruebas

        // Simula la conexión con el servidor Python
        try {
            String response = simulateTicketOCR(file);
            System.out.println("Respuesta del servidor Python: " + response);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static String simulateTicketOCR(File file) throws IOException {
        // Crea una instancia de RestTemplate para enviar la solicitud
        RestTemplate restTemplate = new RestTemplate();

        // Crea el cuerpo de la solicitud con el archivo a enviar
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file);

        // Configuración de los headers para el POST (multipart/form-data)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        // URL simulada del servidor Python
        String pythonServerUrl = "http://localhost:5000/procesar";  // Cambia la URL según tu configuración

        try {
            // Realiza la solicitud POST al servidor Python
            ResponseEntity<String> response = restTemplate.exchange(
                    pythonServerUrl,   // URL del servidor Python
                    HttpMethod.POST,   // Método POST
                    entity,            // Cuerpo de la solicitud (multipart)
                    String.class       // Tipo de respuesta esperado (String)
            );

            // Comprobamos la respuesta
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();  // Devuelve el cuerpo de la respuesta (simulado)
            } else {
                throw new IOException("Error al procesar OCR: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new IOException("Error al enviar la solicitud al servidor Python: " + e.getMessage());
        }
    }
}
