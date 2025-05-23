package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.exceptions.ErrorPharseJsonException;
import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.StatusServerResponse;
import Proyecto.GestorAPI.services.OCRService;
import Proyecto.GestorAPI.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class OCRServiceImpl implements OCRService {

    // Carpeta base donde se almacenan imágenes de tickets
    private static final String STORAGE_BASE_PATH = "gastos/";

    @Value("${python.server.url}")
    private String pythonServerUrl;

    @Value("${python.server.apiKey}")
    private String pythonServerApiKey;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private StorageServiceImpl storageService;

    private final RestTemplate restTemplate;

    // Constructor inyecta RestTemplate
    public OCRServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Procesa una imagen de ticket:
     * - Guarda temporalmente el archivo recibido
     * - Envía el archivo al servidor Python para OCR
     * - Mapea el resultado JSON a objeto Ticket
     * - Guarda imagen y ticket en base de datos
     */
    @Override
    public Ticket processImageTicket(MultipartFile file, User user)  {
        Ticket ticket = new Ticket();
        try {
            System.out.println("Imagen0");
            // 1. Guardar archivo temporalmente para enviar a OCR
            Path tempFilePath = Files.createTempFile("ticket_", "_" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Imagen1");
            // 2. Llamar al servicio OCR enviando el archivo temporal
            String ocrResult = sendFileForOCR(tempFilePath.toFile(), true);

            System.out.println("Imagen2");
            // 3. Convertir el resultado OCR en un Ticket usando TicketService
            ticket = ticketService.mappingCreateTicketbyOCR(ocrResult, user);

            System.out.println("Imagen3");
            // 3.1 Guardar la imagen en almacenamiento y asignar ruta al ticket
            ticket.setIcon(storageService.saveImageData(STORAGE_BASE_PATH, file));

            System.out.println("Imagen4");
            // 4. Guardar el ticket en base de datos
            ticket = ticketService.setItem(ticket);

            return ticket;

        } catch (Exception | ErrorPharseJsonException e) {
            // En caso de error eliminar imagen guardada para evitar basura
            storageService.deleteImageData(ticket.getIcon());
            throw new RuntimeException(e);
        }
    }

    /**
     * Procesa ticket digital (texto o PDF):
     * - Guarda archivo temporalmente
     * - Envía al OCR para archivo digital
     * - Retorna ticket con JSON de productos asignado
     */
    @Override
    public Ticket proccessDigitalTicket(MultipartFile file, User user) throws IOException {
        Path tempFilePath = Files.createTempFile("TicketDigital" + "_", "_" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Digital");
        String ocrResult = sendFileForOCR(tempFilePath.toFile(), false);

        Ticket ticket = new Ticket();
        ticket.setProductsJSON(ocrResult);
        return ticket;
    }

    /**
     * Envía archivo al servidor OCR en Python:
     * - Usa multipart/form-data con autenticación Bearer
     * - Decide endpoint según si es imagen o archivo digital
     * - Maneja errores HTTP y los convierte en IOException
     */
    @Override
    public String sendFileForOCR(File file, boolean imagen) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + pythonServerApiKey);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = imagen ? pythonServerUrl + "/api/ocr" : pythonServerUrl + "/api/ocr-file";

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            System.out.println("Status: " + response.getStatusCode() + " Body: " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error Status: " + ex.getStatusCode() + " Error Body: " + ex.getResponseBodyAsString());
            throw new IOException("Error al procesar OCR. Código: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
        }
    }

    /**
     * Consulta estado del servidor OCR Python.
     * Retorna un objeto con estados booleanos.
     * Si falla, retorna un objeto con todos false.
     */
    public StatusServerResponse getStatus() {
        StatusServerResponse health = new StatusServerResponse(false,false,false);
        try {
            String url = pythonServerUrl + "/status";
            ResponseEntity<StatusServerResponse> response = restTemplate.getForEntity(url, StatusServerResponse.class);
            health = response.getBody();
        } catch (Exception e) {
            System.out.println("Servidor Python no disponible: " + e.getMessage());
        }

        return health;
    }
}
