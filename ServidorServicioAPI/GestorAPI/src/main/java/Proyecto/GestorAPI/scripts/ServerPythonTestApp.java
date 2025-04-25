package Proyecto.GestorAPI.scripts;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

/**
 * Simulador de la aplicación cliente para interactuar con el servidor Python.
 *
 * Este simulador simula la creación de un archivo (por ejemplo, un ticket de compra),
 * y realiza una solicitud HTTP POST al servidor Python para procesar el OCR (Reconocimiento Óptico de Caracteres).
 * El servidor Python debe estar configurado para recibir archivos y responder con los datos procesados.
 */
public class ServerPythonTestApp {

    public static void main(String[] args) {
        // Simula la creación de un archivo temporal (ajustar el nombre del archivo según sea necesario)
        File file = new File("sample_ticket.jpg");

        // Simula la conexión con el servidor Python para procesar el archivo
        try {
            String response = simulateTicketOCR(file);
            System.out.println("Respuesta del servidor Python: " + response);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Simula la solicitud al servidor Python para procesar un archivo con OCR.
     *
     * @param file El archivo de imagen a procesar.
     * @return La respuesta del servidor, que contiene los datos procesados.
     * @throws IOException Si ocurre algún error durante el proceso de la solicitud.
     */
    public static String simulateTicketOCR(File file) throws IOException {
        // Crear una instancia de RestTemplate para realizar solicitudes HTTP
        RestTemplate restTemplate = new RestTemplate();

        // Crear el cuerpo de la solicitud con el archivo a enviar
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file);  // Añadir el archivo a la solicitud

        // Configuración de los headers para una solicitud multipart/form-data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA); // Especificar que el contenido es multipart
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers); // Crear la entidad con los headers y cuerpo

        // URL del servidor Python que procesa el OCR (asegúrate de que la URL sea correcta)
        String pythonServerUrl = "http://localhost:5000/procesar";  // Cambia la URL según la configuración de tu servidor Python

        try {
            // Realizar la solicitud POST al servidor Python
            ResponseEntity<String> response = restTemplate.exchange(
                    pythonServerUrl,       // URL del servidor Python
                    HttpMethod.POST,       // Método de la solicitud (POST)
                    entity,                // Cuerpo de la solicitud (contiene el archivo)
                    String.class           // Tipo de respuesta esperado (String)
            );

            // Verificar si la solicitud fue exitosa (código de estado HTTP 200 OK)
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();  // Devuelve el cuerpo de la respuesta (datos procesados)
            } else {
                // Si la respuesta no es OK, lanzar una excepción con el código de error
                throw new IOException("Error al procesar OCR: " + response.getStatusCode());
            }
        } catch (Exception e) {
            // Capturar cualquier excepción que ocurra durante el proceso de la solicitud
            throw new IOException("Error al enviar la solicitud al servidor Python: " + e.getMessage());
        }
    }
}
